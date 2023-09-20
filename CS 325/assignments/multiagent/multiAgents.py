# THIS CODE IS MY OWN WORK, IT WAS WRITTEN WITHOUT CONSULTING
# A TUTOR OR CODE WRITTEN BY OTHER STUDENTS - Yijun Liu

# multiAgents.py
# --------------
# Licensing Information:  You are free to use or extend these projects for 
# educational purposes provided that (1) you do not distribute or publish 
# solutions, (2) you retain this notice, and (3) you provide clear 
# attribution to UC Berkeley, including a link to 
# http://inst.eecs.berkeley.edu/~cs188/pacman/pacman.html
# 
# Attribution Information: The Pacman AI projects were developed at UC Berkeley.
# The core projects and autograders were primarily created by John DeNero 
# (denero@cs.berkeley.edu) and Dan Klein (klein@cs.berkeley.edu).
# Student side autograding was added by Brad Miller, Nick Hay, and 
# Pieter Abbeel (pabbeel@cs.berkeley.edu).
#
# Modified by Eugene Agichtein for CS325 Sp 2014 (eugene@mathcs.emory.edu)
#

from util import manhattanDistance
from game import Directions
import random, util

from game import Agent

class ReflexAgent(Agent):
    """
      A reflex agent chooses an action at each choice point by examining
      its alternatives via a state evaluation function.

      The code below is provided as a guide.  You are welcome to change
      it in any way you see fit, so long as you don't touch our method
      headers.
    """


    def getAction(self, gameState):
        """
        You do not need to change this method, but you're welcome to.

        getAction chooses among the best options according to the evaluation function.

        Just like in the previous project, getAction takes a GameState and returns
        some Directions.X for some X in the set {North, South, West, East, Stop}
        """
        # Collect legal moves and successor states
        legalMoves = gameState.getLegalActions()

        # Choose one of the best actions
        scores = [self.evaluationFunction(gameState, action) for action in legalMoves]
        bestScore = max(scores)
        bestIndices = [index for index in range(len(scores)) if scores[index] == bestScore]
        chosenIndex = random.choice(bestIndices) # Pick randomly among the best

        "Add more of your code here if you want to"

        return legalMoves[chosenIndex]

    def evaluationFunction(self, currentGameState, action):
        """
        Design a better evaluation function here.

        The evaluation function takes in the current and proposed successor
        GameStates (pacman.py) and returns a number, where higher numbers are better.

        The code below extracts some useful information from the state, like the
        remaining food (newFood) and Pacman position after moving (newPos).
        Note that the successor game state includes updates such as available food,
        e.g., would *not* include the food eaten at the successor state's pacman position
        as that food is no longer remaining.
        newScaredTimes holds the number of moves that each ghost will remain
        scared because of Pacman having eaten a power pellet.

        Print out these variables to see what you're getting, then combine them
        to create a masterful evaluation function.
        """
        # Useful information you can extract from a GameState (pacman.py)
        successorGameState = currentGameState.generatePacmanSuccessor(action)
        newPos = successorGameState.getPacmanPosition()
        newFood = successorGameState.getFood() #food available from successor state (excludes food@successor)
        newGhostStates = successorGameState.getGhostStates()
        newScaredTimes = [ghostState.scaredTimer for ghostState in newGhostStates]

        "*** YOUR CODE HERE ***"
        ghost_distance = float("infinity")
        food_distance = float("infinity")

        for ghost in newGhostStates:
            ghost_distance_temp = manhattan_distance(newPos, ghost.configuration.pos)
            if ghost_distance_temp < ghost_distance:
                ghost_distance = ghost_distance_temp

        newFoodList = newFood.asList()

        for food in newFoodList:
            food_distance_temp = manhattan_distance(newPos, food)
            if food_distance_temp < food_distance:
                food_distance = food_distance_temp

        scare_time = min(newScaredTimes)

        p1 = scare_time * 0.75
        p2 = -1.15 * len(newFoodList)
        if(scare_time <= 0):
            p3 = -2.25 / (ghost_distance + 1)
        else:
            p3 = 0.5/(ghost_distance+1)
        p4 = 0.5/(food_distance+1)

        return p1 + p2 + p3 + p4

def scoreEvaluationFunction(currentGameState):
    """
      This default evaluation function just returns the score of the state.
      The score is the same one displayed in the Pacman GUI.

      This evaluation function is meant for use with adversarial search agents
      (not reflex agents).
    """
    return currentGameState.getScore()

class MultiAgentSearchAgent(Agent):
    """
      This class provides some common elements to all of your
      multi-agent searchers.  Any methods defined here will be available
      to the MinimaxPacmanAgent, AlphaBetaPacmanAgent & ExpectimaxPacmanAgent.

      You *do not* need to make any changes here, but you can if you want to
      add functionality to all your adversarial search agents.  Please do not
      remove anything, however.

      Note: this is an abstract class: one that should not be instantiated.  It's
      only partially specified, and designed to be extended.  Agent (game.py)
      is another abstract class.
    """

    def __init__(self, evalFn = 'scoreEvaluationFunction', depth = '2'):
        self.index = 0 # Pacman is always agent index 0
        self.evaluationFunction = util.lookup(evalFn, globals())
        self.depth = int(depth)

class MinimaxAgent(MultiAgentSearchAgent):
    """
      Your minimax agent (question 2)
    """

    def getAction(self, gameState):
        """
          Returns the minimax action from the current gameState using self.depth
          and self.evaluationFunction.

          Here are some method calls that might be useful when implementing minimax.

          gameState.getLegalActions(agentIndex):
            Returns a list of legal actions for an agent
            agentIndex=0 means Pacman, ghosts are >= 1

          gameState.generateSuccessor(agentIndex, action):
            Returns the successor game state after an agent takes an action

          gameState.getNumAgents():
            Returns the total number of agents in the game
        """
        "*** YOUR CODE HERE ***"

        legalAction = gameState.getLegalActions(0)
        moveState =[gameState.generateSuccessor(0,move) for move in legalAction]
        scores = [self.minimizer(state,0,1) for state in moveState]
        highestScore = max(scores)
        bestMoveIndex = [index for index in range(len(scores)) if scores[index] == highestScore]
        return legalAction[random.choice(bestMoveIndex)]

        util.raiseNotDefined()

    def maximizer(self, gameState, depth):
        if self.depth == depth or gameState.isWin() or gameState.isLose():
            return self.evaluationFunction(gameState)
        moveState = [gameState.generateSuccessor(0, move) for move in gameState.getLegalActions(0)]
        scores = [self.minimizer(state, depth, 1) for state in moveState]
        return max(scores)

    def minimizer(self, gameState, depth, ghost):
        if (self.depth == depth or gameState.isWin() or gameState.isLose()):
            return self.evaluationFunction(gameState)
        moveState = [gameState.generateSuccessor(ghost, move) for move in gameState.getLegalActions(ghost)]
        if ghost >= gameState.getNumAgents() -1:
            scores = [(self.maximizer(state, depth+1)) for state in moveState]
        else:
            scores = [(self.minimizer(state, depth, ghost + 1)) for state in moveState]
        return min(scores)


class AlphaBetaAgent(MultiAgentSearchAgent):
    """
      Your minimax agent with alpha-beta pruning (question 3)
    """

    def getAction(self, gameState):
        """
          Returns the minimax action using self.depth and self.evaluationFunction
        """

        "*** YOUR CODE HERE ***"
        alpha, beta, value = float('-infinity'), float('infinity'), float('-infinity')
        legalAction = gameState.getLegalActions(0)
        bestMove = legalAction[0]

        for action in legalAction:
            moveState = gameState.generateSuccessor(0, action)
            currValue = self.minimizer(moveState, 0, 1, alpha, beta)
            if currValue > value:
                value = currValue
                bestMove = action
            if currValue > beta:
                return bestMove
            alpha = max(alpha, currValue)

        return bestMove
        util.raiseNotDefined()


    def maximizer(self, gameState, depth, alpha, beta):
        if self.depth == depth or gameState.isWin() or gameState.isLose():
            return self.evaluationFunction(gameState)
        legalAction = gameState.getLegalActions(0)
        value = float('-infinity')
        for action in legalAction:
            moveState = gameState.generateSuccessor(0, action)
            value = max(self.minimizer(moveState, depth, 1, alpha, beta), value)
            if value > beta:
                return value
            alpha = max(alpha, value)
        return value

    def minimizer(self, gameState, depth, ghost, alpha, beta):
        if self.depth == depth or gameState.isWin() or gameState.isLose():
            return self.evaluationFunction(gameState)
        legalAction = gameState.getLegalActions(ghost)
        value = float('infinity')
        numGhost = gameState.getNumAgents() -1
        for action in legalAction:
            moveState = gameState.generateSuccessor(ghost, action)
            if ghost >= numGhost:
                value = min(self.maximizer(moveState, depth+1, alpha, beta), value)
            elif ghost < numGhost:
                value = min(self.minimizer(moveState, depth, ghost+1, alpha, beta),value)
            if value < alpha:
                return value
            beta = min(beta, value)
        return value


class ExpectimaxAgent(MultiAgentSearchAgent):
    """
      Your expectimax agent (question 4)
    """

    def getAction(self, gameState):
        """
          Returns the expectimax action using self.depth and self.evaluationFunction

          All ghosts should be modeled as choosing uniformly at random from their
          legal moves.
        """
        legalAction = gameState.getLegalActions(0)
        moveState =[gameState.generateSuccessor(0,move) for move in legalAction]
        scores = [self.exp(state,0,1) for state in moveState]
        highestScore = max(scores)
        bestMoveIndex = [index for index in range(len(scores)) if scores[index] == highestScore]
        return legalAction[random.choice(bestMoveIndex)]
        util.raiseNotDefined()

    def exp(self, gameState, depth, ghost):
        if (self.depth == depth or gameState.isWin() or gameState.isLose()):
            return self.evaluationFunction(gameState)
        moveState = [gameState.generateSuccessor(ghost, move) for move in gameState.getLegalActions(ghost)]
        if ghost >= gameState.getNumAgents() - 1:
            scores = [(self.maximizer(state, depth + 1)) for state in moveState]
        else:
            scores = [(self.exp(state, depth, ghost + 1)) for state in moveState]
        return sum(scores)/len(scores)


    def maximizer(self, gameState, depth):
        if self.depth == depth or gameState.isWin() or gameState.isLose():
            return self.evaluationFunction(gameState)
        moveState = [gameState.generateSuccessor(0, move) for move in gameState.getLegalActions(0)]
        scores = [self.exp(state, depth, 1) for state in moveState]
        return max(scores)

def betterEvaluationFunction(currentGameState):
    """
      Your extreme ghost-hunting, pellet-nabbing, food-gobbling, unstoppable
      evaluation function (question 5).

      DESCRIPTION: <write something here so we know what you did>
    """
    "*** YOUR CODE HERE ***"
    newPos = currentGameState.getPacmanPosition()
    newFood = currentGameState.getFood()
    newGhostStates = currentGameState.getGhostStates()
    newScaredTimes = [ghostState.scaredTimer for ghostState in newGhostStates]
    newFoodList = newFood.asList()
    newCapsules = currentGameState.getCapsules()

    ghost_distance = float("infinity")
    food_distance = float("infinity")
    capsules_distance = float("infinity")

    for ghost in newGhostStates:
        ghost_distance_temp = manhattan_distance(newPos, ghost.configuration.pos)
        if ghost_distance_temp < ghost_distance:
            ghost_distance = ghost_distance_temp

    for food in newFoodList:
        food_distance_temp = manhattan_distance(newPos, food)
        if food_distance_temp < food_distance:
            food_distance = food_distance_temp

    for capsules in newCapsules:
        capsules_distance_temp = manhattan_distance(newPos, capsules)
        if capsules_distance_temp < capsules_distance:
            capsules_distance = capsules_distance_temp

    scare_time = min(newScaredTimes)

    # scare time is positive effect to the pacman
    p1 = scare_time * 0.5

    # pacman should keep moving with a high current score to keep the strike and achieve higher scores
    p6 = currentGameState.getScore()
    p2 = -1 * len(newFoodList)

    # ghosts are terrible if there's no scare time, but not bad if there's scare time
    if (scare_time <= 0):
        p3 = -1.8 / (ghost_distance + 1)
    else:
        p3 = 0.5 / (ghost_distance + 1)

    # consider food distance and ghost distance together because travel increases the chance of meeting ghost;
    # higher combine distance, lower incentives
    p4 = 0.5 / (food_distance + 1 + ghost_distance)

    # further away from the capsules, less incentives
    p5 = 0.6 / (capsules_distance + 1)

    return p1 + p2 + p3 + p4 + p5 + p6
    util.raiseNotDefined()

# Abbreviation
better = betterEvaluationFunction


class ContestAgent(MultiAgentSearchAgent):
    """
    Your agent for the mini-contest
    """

    def __init__(self, evalFn='miniContestEvaluation'):
        self.index = 0
        self.evaluationFunction = util.lookup(evalFn, globals())
        self.depth = 3

    def getAction(self, gameState):

        # for the mini-contest, I used the alpha-beta purning search to maximize the time efficiency by expanding less nodes
        alpha, beta, value = float('-infinity'), float('infinity'), float('-infinity')
        legalAction = gameState.getLegalActions(0)
        bestMove = legalAction[0]

        for action in legalAction:
            moveState = gameState.generateSuccessor(0, action)
            currValue = self.minimizer(moveState, 0, 1, alpha, beta)
            if currValue > value:
                value = currValue
                bestMove = action
            if currValue > beta:
                return bestMove
            alpha = max(alpha, currValue)

        return bestMove
    def maximizer(self, gameState, depth, alpha, beta):
        if self.depth == depth or gameState.isWin() or gameState.isLose():
            return self.evaluationFunction(gameState)
        legalAction = gameState.getLegalActions(0)
        value = float('-infinity')
        for action in legalAction:
            moveState = gameState.generateSuccessor(0, action)
            value = max(self.minimizer(moveState, depth, 1, alpha, beta), value)
            if value > beta:
                return value
            alpha = max(alpha, value)
        return value

    def minimizer(self, gameState, depth, ghost, alpha, beta):
        if self.depth == depth or gameState.isWin() or gameState.isLose():
            return self.evaluationFunction(gameState)
        legalAction = gameState.getLegalActions(ghost)
        value = float('infinity')
        numGhost = gameState.getNumAgents() -1
        for action in legalAction:
            moveState = gameState.generateSuccessor(ghost, action)
            if ghost >= numGhost:
                value = min(self.maximizer(moveState, depth+1, alpha, beta), value)
            elif ghost < numGhost:
                value = min(self.minimizer(moveState, depth, ghost+1, alpha, beta),value)
            if value < alpha:
                return value
            beta = min(beta, value)
        return value


def miniContestEvaluation(currentGameState):
    newPos = currentGameState.getPacmanPosition()
    newFood = currentGameState.getFood()
    newGhostStates = currentGameState.getGhostStates()
    newScaredTimes = [ghostState.scaredTimer for ghostState in newGhostStates]
    newFoodList = newFood.asList()
    newCapsules = currentGameState.getCapsules()

    ghost_distance = float("infinity")
    food_distance = float("infinity")
    capsules_distance = float("infinity")

    for ghost in newGhostStates:
        ghost_distance_temp = manhattan_distance(newPos, ghost.configuration.pos)
        if ghost_distance_temp < ghost_distance:
            ghost_distance = ghost_distance_temp

    for food in newFoodList:
        food_distance_temp = manhattan_distance(newPos, food)
        if food_distance_temp < food_distance:
            food_distance = food_distance_temp

    for capsules in newCapsules:
        capsules_distance_temp = manhattan_distance(newPos, capsules)
        if capsules_distance_temp < capsules_distance:
            capsules_distance = capsules_distance_temp

    scare_time = min(newScaredTimes)

    # scare time is positive effect to the pacman
    p1 = scare_time * 0.9

    # pacman should keep moving with a high current score to keep the strike and achieve higher scores
    p6 = .1 * currentGameState.getScore()

    p2 = -.5 * len(newFoodList)

    # ghosts are terrible if there's no scare time, but not bad if there's scare time
    if (scare_time <= 0):
        p3 = -1.4 / (ghost_distance + 1)
    else:
        p3 = 1.6 / (ghost_distance + 1)

    # consider food distance and ghost distance together; higher combine distance, lower incentives

    p4 = 2.8 / (food_distance + 1 + ghost_distance)

    # further away from the capsules, less incentives
    p5 = 2.2 / (capsules_distance + 1)

    return p1 + p2 + p3 + p4 + p5 + p6
    util.raiseNotDefined()

def manhattan_distance(p1, p2):
    return abs(p1[0] - p2[0]) + abs(p1[1] - p2[1])