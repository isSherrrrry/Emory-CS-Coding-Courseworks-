# ========================================================================
# Copyright 2022 Emory University
#
# Licensed under the Apache License, Version 2.0 (the `License`);
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an `AS IS` BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
# ========================================================================
__author__ = 'Yijun Liu'

from enum import Enum
from emora_stdm import DialogueFlow
from resources.utils import MacroGPTJSON
import json
import re
from json import JSONDecodeError
from typing import Dict, Any, List, Callable, Pattern
import openai
from emora_stdm import Macro, Ngrams
from resources import regexutils


PATH_API_KEY = '../../resources/openai_api.txt'

available_times = {
    'haircut': {
        "Monday": ["10:00", "13:00", "14:00"],
        "Tuesday": ["14:00"]
    },
    'coloring': {
        "Wednesday": ["10:00", "11:00", "13:00"],
        "Thursday": ["10:00", "11:00"]
    },
    'perms': {
        "Friday": ["10:00", "11:00", "13:00", "14:00"],
        "Saturday": ["10:00", "14:00"]
    }
}

class V(Enum):
    service = 0,
    datetime = 1

def main() -> DialogueFlow:
    transitions = {
        'state': 'start',
        '`Hello, this is Wonder Hair Salon. How can I help you? What services are you looking for?`': {
            '#SET_SERVICE': {
                '#IF($MULTI>1) `Sure. You have request multiple services. Let\'s walk through them.`': 'multi',
                '`Sure. You are asking for a`$SERVICE`service. What date and time are you looking for?`': {
                    'score': 0.4,
                    '#SET_DATETIME': {
                        '`Your appointment is set on`$DAY`at`$TIME`. See you!`': 'end'
                    },
                    'error': {
                        '`Sorry, that slot is not available for the chosen service. '
                        'Here are the available times for the service:`$AVAILTIME`'
                        'Do you want to book again?`': {
                            '[{yes, yeah, absolutely, definitely, sure, certainly, of course, right, it is, '
                            'probably, maybe, you bet, yep, well, gladly, okay, ok, yea, agree'
                            'affirmative, by all means, indeed, without a doubt, positive}]': 'start',
                            '[{not, no, nope, sorry, unfortunately, do not, don, never, can not, cannot}]': 'end'
                        }
                    }
                }
            },
            'error': {
                '`Sorry, we do not provide that service. We only provide haircut, hair coloring, and hair perms. '
                'Do you want to book again?`': {
                    '[{yes, yeah, absolutely, definitely, sure, certainly, of course, right, it is, '
                        'probably, maybe, you bet, yep, well, gladly, okay, ok, yea, agree'
                        'affirmative, by all means, indeed, without a doubt, positive}]': 'start',
                    '[{not, no, nope, sorry, unfortunately, do not, don, never, can not, cannot}]': 'end'
                }
            }
        }
    }

    transitions_multi = {
        'state': 'multi',
        '`You are asking for`$MULTI_SERVICE`service. What date and time are you looking for?\n'
        'Please give me all your available times for all services.`': {
            '#SET_DATETIME': {
                '`Cool!`$SETENCE`See you!`': 'end'
            },
            'error': {
                '`Sorry, not all your services can be booked based on your availabilities. '
                'Here are the available times for all services:\n'
                'Haircut: Monday: 10:00, 13:00, 14:00. Tuesday: 14:00.\n'
                'Perms: Friday: 10:00, 11:00, 13:00, 14:00. Saturday: 10:00, 14:00.\n'
                'Coloring: Wednesday: 10:00, 11:00, 13:00. Thursday: 10:00, 11:00.``'
                'Do you want to book again?`': {
                    '[{yes, yeah, absolutely, definitely, sure, certainly, of course, right, it is, '
                    'probably, maybe, you bet, yep, well, gladly, okay, ok, yea, agree'
                    'affirmative, by all means, indeed, without a doubt, positive}]': 'multi',
                    '[{not, no, nope, sorry, unfortunately, do not, don, never, can not, cannot}]': 'end'
                }
            }
        }
    }

    macros = {
        'SET_SERVICE': MacroGPTJSON(
            'What service does the user want to book in our hair salon? '
            'Please ONLY choose from the following options using the exact word: haircut, coloring, or perms.',
            {V.service.name: ["haircut", "coloring"]}
        ),
        'SET_DATETIME': MacroGPTJSON_TIME(
            'What date and time does the user want to book?',
            {V.datetime.name: [{"day": "Tuesday", "time": "14:00"}, {"day": "Tuesday", "time": "14:00"}]},
        )
    }

    df = DialogueFlow('start', end_state='end')
    df.load_transitions(transitions)
    df.load_transitions(transitions_multi)
    df.add_macros(macros)
    return df


OPENAI_API_KEY_PATH = '../../resources/openai_api.txt'
CHATGPT_MODEL = 'gpt-3.5-turbo'


class MacroGPTJSON(Macro):
    def __init__(self, request: str, full_ex: Dict[str, Any], empty_ex: Dict[str, Any] = None, set_variables: Callable[[Dict[str, Any], Dict[str, Any]], None] = None):
        """
        :param request: the task to be requested regarding the user input (e.g., How does the speaker want to be called?).
        :param full_ex: the example output where all values are filled (e.g., {"call_names": ["Mike", "Michael"]}).
        :param empty_ex: the example output where all collections are empty (e.g., {"call_names": []}).
        :param set_variables: it is a function that takes the STDM variable dictionary and the JSON output dictionary and sets necessary variables.
        """
        self.request = request
        self.full_ex = json.dumps(full_ex)
        self.empty_ex = '' if empty_ex is None else json.dumps(empty_ex)
        self.check = re.compile(regexutils.generate(full_ex))
        self.set_variables = set_variables

    def run(self, ngrams: Ngrams, vars: Dict[str, Any], args: List[Any]):
        examples = f'{self.full_ex} or {self.empty_ex} if unavailable' if self.empty_ex else self.full_ex
        prompt = f'{self.request} Respond in the JSON schema such as {examples}: {ngrams.raw_text().strip()}'
        output = gpt_completion(prompt)
        if not output: return False

        try:
            d = json.loads(output)
        except JSONDecodeError:
            print(f'Invalid: {output}')
            return False

        vars['MULTI'] = 0

        if len(d['service']) > 1:
            vars['MULTI'] = len(d['service'])

        vars['HAIRCUT'] = False
        vars['PERMS'] = False
        vars['COLORING'] = False

        if 'service' in d:
            for service in d['service']:
                if service.lower() == 'haircut':
                    vars['HAIRCUT'] = True
                elif service.lower() == 'perms':
                    vars['PERMS'] = True
                elif service.lower() == 'coloring':
                    vars['COLORING'] = True

        if (vars['HAIRCUT'] and vars['PERMS'] and vars['COLORING']):
            vars['MULTI_SERVICE'] = 'haircut, perms, and coloring'
        elif(vars['HAIRCUT'] and vars['PERMS']):
            vars['MULTI_SERVICE'] = 'haircut and perms'
        elif (vars['HAIRCUT'] and vars['COLORING']):
            vars['MULTI_SERVICE'] = 'haircut and coloring'
        elif (vars['PERMS'] and vars['COLORING']):
            vars['MULTI_SERVICE'] = 'perms and coloring'

        if self.set_variables:
            self.set_variables(vars, d)
            if(d["service"]):
                vars['SERVICE'] = d["service"][0]
        else:
            vars.update(d)
            if (d["service"]):
                vars['SERVICE'] = d["service"][0]

        if(vars['SERVICE'] and vars['MULTI'] == 0):
            vars['AVAILTIME'] = get_available_times_string(vars['SERVICE'])

        return True

class MacroGPTJSON_TIME(Macro):
    def __init__(self, request: str, full_ex: Dict[str, Any], empty_ex: Dict[str, Any] = None, set_variables: Callable[[Dict[str, Any], Dict[str, Any]], None] = None):
        """
        :param request: the task to be requested regarding the user input (e.g., How does the speaker want to be called?).
        :param full_ex: the example output where all values are filled (e.g., {"call_names": ["Mike", "Michael"]}).
        :param empty_ex: the example output where all collections are empty (e.g., {"call_names": []}).
        :param set_variables: it is a function that takes the STDM variable dictionary and the JSON output dictionary and sets necessary variables.
        """
        self.request = request
        self.full_ex = json.dumps(full_ex)
        self.empty_ex = '' if empty_ex is None else json.dumps(empty_ex)
        self.check = re.compile(regexutils.generate(full_ex))
        self.set_variables = set_variables

    def run(self, ngrams: Ngrams, vars: Dict[str, Any], args: List[Any]):
        examples = f'{self.full_ex} or {self.empty_ex} if unavailable' if self.empty_ex else self.full_ex
        prompt = f'{self.request} Respond in the JSON schema such as {examples}: {ngrams.raw_text().strip()}'
        output = gpt_completion(prompt)
        if not output: return False

        try:
            d = json.loads(output)
        except JSONDecodeError:
            print(f'Invalid: {output}')
            return False
        if self.set_variables:
            self.set_variables(vars, d)
        else:
            vars.update(d)

        vars['SETENCE'] = None

        if d['datetime'] and vars['MULTI'] == 0:
            pair = check_availability(vars['SERVICE'], d['datetime'])
            if pair is not None:
                vars['DAY'] = pair[0]
                vars['TIME'] = pair[1]
                return True
        elif d['datetime'] and vars['MULTI'] == 2:
            if vars['HAIRCUT'] and vars['PERMS']:
                pair1 = check_availability('haircut', d['datetime'])
                pair2 = check_availability('perms', d['datetime'])
                if pair1 and pair2:
                    vars['SETENCE'] = "Your haircut service is on {} at {} and your perms service is on {} at {}.".format(pair1[0], pair1[1], pair2[0], pair2[1])
                    return True
            if vars['HAIRCUT'] and vars['COLORING']:
                pair1 = check_availability('haircut', d['datetime'])
                pair2 = check_availability('coloring', d['datetime'])
                if pair1 and pair2:
                    vars['SETENCE'] = "Your haircut service is on {} at {} and your coloring service is on {} at {}.".format(pair1[0], pair1[1], pair2[0], pair2[1])
                    return True
            if vars['COLORING'] and vars['PERMS']:
                pair1 = check_availability('coloring', d['datetime'])
                pair2 = check_availability('perms', d['datetime'])
                if pair1 and pair2:
                    vars['SETENCE'] = "Your coloring service is on {} at {} and your perms service is on {} at {}.".format(pair1[0], pair1[1], pair2[0], pair2[1])
                    return True
        elif d['datetime'] and vars['MULTI'] == 3:
            pair1 = check_availability('perms', d['datetime'])
            pair3 = check_availability('haircut', d['datetime'])
            pair2 = check_availability('coloring', d['datetime'])
            if pair1 and pair2 and pair3:
                vars['SETENCE'] = "Your perms service is on {} at {}, your haircut service is on {} at {}, and your coloring service is on {} at {}.".format(pair1[0], pair1[1], pair2[0], pair2[1],pair3[0], pair3[1])
                return True
        return False

def check_availability(service, datetime_list):
    for datetime_entry in datetime_list:
        global_day = datetime_entry['day']
        global_time = datetime_entry['time']
        if global_day in available_times[service] and global_time in available_times[service][global_day]:
            pair = [global_day, global_time]
            return pair
    return None



def get_available_times_string(service):
    time_string = ""
    for day, times in available_times[service].items():
        time_string += f"{day}: {', '.join(times)}. "
    return time_string

class MacroNLG(Macro):
    def __init__(self, generate: Callable[[Dict[str, Any]], str]):
        self.generate = generate

    def run(self, ngrams: Ngrams, vars: Dict[str, Any], args: List[Any]):
        return self.generate(vars)


def gpt_completion(input: str, regex: Pattern = None) -> str:
    response = openai.ChatCompletion.create(
        model=CHATGPT_MODEL,
        messages=[{'role': 'user', 'content': input}]
    )
    output = response['choices'][0]['message']['content'].strip()

    if regex is not None:
        m = regex.search(output)
        output = m.group().strip() if m else None

    return output

def set_service(vars: Dict[str, Any], user: Dict[str, Any]):
    vars[V.service.name] = user[V.service.name]


if __name__ == '__main__':
    openai.api_key_path = OPENAI_API_KEY_PATH
    main().run()
