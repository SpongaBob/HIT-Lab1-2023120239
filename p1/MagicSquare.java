package p1;

import java.io.*;

public class MagicSquare {
    public static void main(String[] args) throws IOException {
//        System.out.println(isLegalMagicSquare("src/txt/1.txt"));
//        System.out.println(isLegalMagicSquare("src/txt/2.txt"));
//        System.out.println(isLegalMagicSquare("src/txt/3.txt"));
//        System.out.println(isLegalMagicSquare("src/txt/4.txt"));
//        System.out.println(isLegalMagicSquare("src/txt/5.txt"));

        generateMagicSquare(7);
        System.out.println(isLegalMagicSquare("src/txt/6.txt"));

    }
    public static boolean isLegalMagicSquare(String fileName) throws FileNotFoundException{
        float[][] magicSquare = new float[200][200];
        int col = 0, row = 0;
        File file = new File(fileName);
        Reader reader = new InputStreamReader(new FileInputStream(file));
        BufferedReader buffReader = new BufferedReader(reader);
        try{
            String dataLine = "00";
            int circleTimes = 0;
            int index = 0;
            while(true){
                // read file data by line
                dataLine = buffReader.readLine();
//                System.out.println(dataLine);
                if(dataLine == null){
                    row = circleTimes -1;
                    col = index;
                    if(row != col){
                        System.out.println("非nxn矩阵");
                        return false;
                    }
                    break;
                }
                index = 0;
                String element = "";
                for (int i = 0; i < dataLine.length(); i++){
                    if( dataLine.charAt(i) >= '0' && dataLine.charAt(i) <= '9'){
                        element = element + dataLine.charAt(i);
                    }
                    else if (dataLine.charAt(i) == '\t' && !element.isEmpty() && i != dataLine.length()-1) {
                        magicSquare[circleTimes][index] = Float.parseFloat(element);
                        index++;
                        element = "";
                    }
                }
                if(!element.isEmpty()){
                    magicSquare[circleTimes][index] = Float.parseFloat(element);
                    element = "";
                }
                circleTimes++;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        //
        float colSum = -99, rowSum = -99;
        for(int i=0; i <= col; i++){
            float tempSum0 = 0, tempSum1 = 0;
            for(int j=0; j<=row; j++){
                tempSum0 += magicSquare[i][j];
                tempSum1 += magicSquare[j][i];
            }
            if(tempSum0 != tempSum1 ){
                System.out.printf("第%d行%d列之和不一致",i, i);
                return false;
            }
            else if (tempSum0 != colSum && colSum != -99) {
                System.out.printf("第%d行与%d行之和不一致\n",i, i-1);
                return false;
            }
            else if(tempSum1 != rowSum && rowSum != -99){
                System.out.printf("第%d列与%d列之和不一致\n",i, i-1);
                return false;
            }
            colSum = tempSum0;
            rowSum = tempSum1;

        }
        return true;
    }




    public static boolean generateMagicSquare(int n) throws IOException {
        FileWriter writer = new FileWriter("src/txt/6.txt");
        BufferedWriter buffWriter = new BufferedWriter(writer);
        int[][] magic = new int[n][n];
        int row = 0, col = n/2, i, j, square = n*n;

        for(i = 1; i <= square; i++){
            magic[row][col] = i;
            if(i % n == 0){
                row++;
            }
            else{
                if(row == 0){
                    row = n - 1;
                }
                else{
                    row--;
                }
                if (col == (n-1)){
                    col = 0;
                }
                else{
                    col++;
                }
            }
        }
        for(i = 0; i < n; i++){
            for(j = 0; j < n; j++){
                buffWriter.write(magic[i][j] + "\t");
                System.out.println(magic[i][j] + "\t");
            }
            buffWriter.write("\n");
            System.out.println();
        }
        buffWriter.close();
        writer.close();
        return true;
    }



}