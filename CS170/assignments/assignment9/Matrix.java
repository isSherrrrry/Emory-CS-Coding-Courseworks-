public class Matrix {

    private double[][] matrix = new  double[100][100];
    private int row, column;

    Matrix(double input[][]){
        row = input.length;
        column = input[0].length;
        for (int i = 0; i < row; i++){
            for (int j = 0; j<column; j++){
                matrix[i][j] = input[i][j];
            }
        }
    }

    Matrix(int a, int b){
        row = a;
        column = b;

        for (int i = 0; i < a; i++){
            for (int j = 0; j<b; j++){
                matrix[i][j] = 0;
            }
        }
    }

    public String toString(){
        String str = new String();
        for (int i = 0; i < row; i++){
            for (int j = 0; j<column; j++){
               str += matrix[i][j] + " \t";
            }
            str += "\n";
        }
        return str;
    }

    public int numRows(){
        return row;
    }

    public int numCols(){
        return column;
    }

    public double getElement(int r, int c){
        return matrix[r][c];
    }

    public void setElement(int r, int c, double value){
        matrix[r][c] = value;
    }

    public static Matrix sum(Matrix a, Matrix b){
        if (a.column != b.column || a.row != b.row) return null;
       Matrix isSum = new Matrix(a.row, a.column);
       isSum.row = a.row;
       isSum.column = a.column;
        for (int i = 0; i < a.row; i++){
            for (int j = 0; j<a.column; j++){
                isSum.matrix[i][j] = a.matrix[i][j] + b.matrix[i][j];
            }
        }
        return isSum;
    }

    public static Matrix product(Matrix a, Matrix b){
        if (a.column != b.row) return null;
        Matrix isProduct = new Matrix(a.row, b.column);
        isProduct.row = a.row;
        isProduct.column = b.column;
        for (int i = 0; i < a.row; i++){
            for (int j = 0; j < b.column; j++){
                for (int k = 0; k < a.column; k++){
                    isProduct.matrix[i][j] = isProduct.matrix[i][j] + a.matrix[i][k] * b.matrix[k][j];
                }
            }
        }
        return isProduct;
    }

    public String subMatrix(int deletedRow, int deletedCol){
        if (deletedCol<0 || deletedRow <0 || deletedCol> column || deletedRow > row || row == 1|| column == 1) return null;
        double[][] subR = new double[row+2][column+2];
        double[][] subC = new double[row+2][column+2];
        double[][] result = new double[row-1][column-1];

        for (int i = 0; i < deletedRow; i++){
            for (int j = 0; j<column; j++){
                subR[i][j] = matrix[i][j];
            }
        }

        for (int i = deletedRow+1; i < row; i++){
            for (int j = 0; j<column; j++){
                subR[i-1][j] = matrix[i][j];
            }
        }

        for (int i = 0; i < deletedCol; i++){
            for (int j = 0; j<row; j++){
                subC[j][i] = subR[j][i];
            }
        }

        for (int i = deletedCol+1; i < column; i++){
            for (int j = 0; j<row; j++){
                subC[j][i-1] = subR[j][i];
            }
        }

        for (int i = 0; i < row-1; i++){
            for (int j = 0; j<column-1; j++){
                result[i][j] = subC[i][j];
            }
        }

        String str = new String();
        for (int i = 0; i < row-1; i++){
            for (int j = 0; j<column-1; j++){
                str += result[i][j] + " \t";
            }
            str += "\n";
        }
        return str;

    }

    public static double[][] subMatrix(double[][] m, int deletedRow, int deletedCol){
        int n = m.length;
        double[][] subR = new double[n+2][n+2];
        double[][] subC = new double[n+2][n+2];
        double[][] result = new double[n-1][n-1];

        for (int i = 0; i < deletedRow; i++){
            for (int j = 0; j<n; j++){
                subR[i][j] = m[i][j];
            }
        }

        for (int i = deletedRow+1; i < n; i++){
            for (int j = 0; j<n; j++){
                subR[i-1][j] = m[i][j];
            }
        }

        for (int i = 0; i < deletedCol; i++){
            for (int j = 0; j<n; j++){
                subC[j][i] = subR[j][i];
            }
        }

        for (int i = deletedCol+1; i < n; i++){
            for (int j = 0; j<n; j++){
                subC[j][i-1] = subR[j][i];
            }
        }

        for (int i = 0; i < n-1; i++){
            for (int j = 0; j<n-1; j++){
                result[i][j] = subC[i][j];
            }
        }


        return result;

    }

    public String determinant(){
        if (row != column) return null;
        double[][] matrix1 = new double[row][column];
        for (int i = 0; i < row; i++){
            for (int j = 0; j<column; j++){
                matrix1[i][j] = matrix[i][j];
            }
        }
        String result = "" + deter(matrix1);

        return result;
    }




    public double deter(double[][] matrixfordet) {
        int a = matrixfordet.length;

        if (a == 1) {
            return matrixfordet[0][0];
        }
        else {
            double result = 0;
            for (int i = 0; i < a; i++) {
                result += Math.pow(-1, i) * matrixfordet[0][i] * deter(subMatrix(matrixfordet,0, i));
            }
            return result;
        }

    }
}
