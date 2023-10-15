import java.util.*;
import java.io.*;
public class NestedIfElse {
    /*
     * This is a program that checks the syntax of the nested if else 
     * statement. 
     */
    public static void main(String[] args) {
        System.out.print("==============\nBeginTest\n==============");
        System.out.println();
        //List<String> tokens = new ArrayList<>();
        HashMap<Integer, String> token = new HashMap<Integer, String>();
        int ifCount = 0;
        int elseCount = 0;
        try{
            File file=new File("./InputMedium.txt");    //creates a new file instance  
            FileReader fr=new FileReader(file);   //reads the file  
            BufferedReader br=new BufferedReader(fr);  
            System.out.println("Importing code");
            int curlyCount = 0;
            int pharCount = 0; 
            int LineCount = 0;
            String inp;
            while ((inp=br.readLine())!=null) {
                //System.out.println("Inserting " + inp);
                if(inp.contains("{")){
                    //System.out.println("curlyCount++");
                    curlyCount++;
                }
                if(inp.contains("}")){
                    //System.out.println("curlyCount--");
                    curlyCount--;
                }

                if(inp.contains("(")){
                    //System.out.println("PharCount++");
                    pharCount++;
                }
                if(inp.contains(")")){
                    //System.out.println("PharCount--");
                    pharCount--;
                }
                if(inp.toUpperCase().contains("IF")){
                    ifCount++;
                }else if(inp.toUpperCase().contains("ELSE")){
                    elseCount++;
                }
                //System.out.println("Curly braces count: " + curlyCount + "\nPharanthesis Count: " + pharCount); 
                //tokens.add(inp);
                if(!inp.equals("")){
                    token.put(LineCount,inp);
                    LineCount++;
                }
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
       
        System.out.println("===============");
        System.out.println("\nPrinting tokens");
        //System.out.println("Token list: " + tokens);
        //System.out.println("Token list: " + tokens);
        System.out.println();
        System.out.println("===============");
        System.out.println("Count");
        System.out.println("Number of if statements: " + ifCount);
        System.out.println("Number of else statements: " + elseCount);
        System.out.println("===============");
        System.out.println();
        //int lineNumber = 0;
        // for(String t : tokens){
        //     System.out.println(lineNumber + " | " + t);
        //     lineNumber++;
        // }
        int[] brackets = correctBrackets(token);
        if(brackets[0] == 1){
            ArrayList<Integer> flag_lineNum = scanIF(token, false);
            String[] error = {"Spelling error for 'if'", "Spelling error for 'else'", 
            "Condition operator error", "Empty condition", "Incorrect use of logical operator", 
            "Missing condition", "Missing bracket"};
            if(flag_lineNum.get(0) == 0){
                int temp = 0;
                int line = 0;
                for (int i = 1; i < flag_lineNum.size(); i++) {
                    if(i % 2 == 1){
                        temp = flag_lineNum.get(i);
                    }
                    else{
                        line = flag_lineNum.get(i);
                        System.out.println(error[temp] + " in line " + line);
                    }
                
                }
            }
            else {
                System.out.println("Syntax is correct.");
            }
        }else{
            if (brackets[1] == 1) {
                System.out.println("Excess bracket/s.");
            }else{
                System.out.println("Missing bracket/s.");
            }
        }
        
    }

    static ArrayList<Integer> syntaxCheck(HashMap<Integer, String> ifBlock){
        ArrayList<Integer> lineNum = new ArrayList<>();
        ArrayList<Integer> flag_lineNum = new ArrayList<>();
        int flag = 1;

        // int[] if_block = get_if_block(ifBlock);
        // if(if_block[0] == 0){
        //     flag = if_block[0];
        //     lineNum.add(6);
        //     lineNum.add(if_block[1]);
        //     flag_lineNum.add(0, flag);
        //     flag_lineNum.addAll(lineNum);
        //     return flag_lineNum;
        // }

        for(Map.Entry<Integer, String> met: ifBlock.entrySet()){
            String s = met.getValue();
            int line = met.getKey();
            String temp = s.toUpperCase();
            String[] comparisonOperator = {"<", "<=", "==", "!=", ">", ">="};
            if(temp.contains("IF")){
                if(s.contains("if")){
                    flag = 1;
                }
                else{
                    flag = 0;
                    lineNum.add(0);
                    lineNum.add(line);
                    flag_lineNum.add(0, flag);
                    flag_lineNum.addAll(lineNum);
                    return flag_lineNum;
                }
                String condition = "";
                try {
                    condition = s.substring(s.indexOf('('), s.lastIndexOf(')') + 1);
                } catch (IndexOutOfBoundsException e) {
                    flag = 0;
                    lineNum.add(5);
                    lineNum.add(line);
                    flag_lineNum.add(0, flag);
                    flag_lineNum.addAll(lineNum);
                    return flag_lineNum;
                }
                
                condition = condition.replaceAll(" ", "");
                if(condition.indexOf(')') - condition.indexOf('(') == 1){
                    flag = 0;
                    lineNum.add(3);
                    lineNum.add(line);
                    flag_lineNum.add(0, flag);
                    flag_lineNum.addAll(lineNum);
                    return flag_lineNum;
                }
                ArrayList<String> conditions = new ArrayList<>();
                if(condition.contains("&") || condition.contains("|")){
                    if(condition.contains("&&") || condition.contains("||")){
                        if(condition.contains("&&")){
                            String[] seperateConditions = condition.split("&&");
                            for(String sep: seperateConditions){
                                conditions.add(sep);
                            }
                        }
                        else{
                            String[] seperateConditions = condition.split("||");
                            for(String sep: seperateConditions){
                                conditions.add(sep);
                            }
                        }
                    }
                    else{
                        flag = 0;
                        lineNum.add(4);
                        lineNum.add(line);
                        flag_lineNum.add(0, flag);
                        flag_lineNum.addAll(lineNum);
                        return flag_lineNum;
                    }
                }
                if(conditions.size() < 1){
                    for(int i = 0; i < comparisonOperator.length; i++){
                        if(condition.contains(comparisonOperator[i])){
                            flag = 1;
                            break;
                        }
                        else{
                            flag = 0;
                            if(i == comparisonOperator.length - 1){
                                lineNum.add(2);
                                lineNum.add(line);
                                flag_lineNum.add(0, flag);
                                flag_lineNum.addAll(lineNum);
                                return flag_lineNum;
                            }
                        }
                    }
                }
                else{
                    for(String cond: conditions){
                        for(int i = 0; i < comparisonOperator.length; i++){
                            if(cond.contains(comparisonOperator[i])){
                                flag = 1;
                                break;
                            }
                            else{
                                flag = 0;
                                if(i == comparisonOperator.length - 1){
                                    lineNum.add(2);
                                    lineNum.add(line);
                                    flag_lineNum.add(0, flag);
                                    flag_lineNum.addAll(lineNum);
                                    return flag_lineNum;
                                }
                            }
                        }
                    }
                }
            }
            if(temp.contains("ELSE")){
                if(s.contains("else"))
                    flag = 1;
                else{
                    flag = 0;
                    lineNum.add(1);
                    lineNum.add(line);
                    flag_lineNum.add(0, flag);
                    flag_lineNum.addAll(lineNum);
                    return flag_lineNum;
                }
            }
        }
        flag_lineNum.add(0, flag);
        flag_lineNum.addAll(lineNum);
        // int start = Integer.parseInt(temp[1]);
        // String if_block = temp[0];
        // String else_block = get_else_block(ifBlock, start);
        // System.out.println("If block:\n" + if_block);
        // System.out.println("Else block:\n" + else_block);
        return flag_lineNum;
    }

    public static int[] correctBrackets(HashMap<Integer, String> token){
        int flag = 1;
        int[] ret = new int[2];
        int curlyCount = 0;
        for(Map.Entry<Integer, String> set: token.entrySet()){
            String inp = set.getValue();

            if(inp.contains("}")){
                curlyCount--;
            }
            if(inp.contains("{")){
                curlyCount++;
                int open = inp.indexOf("{");
                while(true){
                    inp = inp.substring(open + 1);
                    if(inp.contains("{")){
                        curlyCount++;
                        open = inp.indexOf("{");
                    }
                    else
                        break;
                }
            }
        }
        
        if(curlyCount != 0){
            flag = 0;
        }
        ret[0] = flag;
        if(curlyCount > 0)
            ret[1] = 1;
        else
            ret[1] = 0;
        
        return ret;
    }
    

    public static String get_else_block(HashMap<Integer, String> token, int start){
        String block = "";
        int curlyCount = 1;
        for(int i = start + 1; i < token.size(); i++){
            String inp = token.get(i);
            if(inp.contains("{")){
                curlyCount++;
            }
            if(inp.contains("}")){
                curlyCount--;
                if(curlyCount == 0){
                    break;
                }
            }
            if (curlyCount > 0) {
                block += inp + "\n";
            }
        }
        return block;
    }

    static ArrayList<Integer> scanIF(HashMap<Integer, String> token, boolean skipIF){
        HashMap<Integer, String> ifBlock = new HashMap<Integer, String>();
        int curlyCount = 0;
        int pharCount = 0; 
        boolean addBlock = false;
        for(Map.Entry<Integer, String> met: token.entrySet()){
            String s = met.getValue();
            int lineNum = met.getKey();
            // System.out.println(met.getKey() + " || " + met.getValue());  
            if(skipIF == false){
                if(s.toUpperCase().contains("IF")){
                    addBlock = true;
                }  
            }
            else{
                skipIF = false;
                curlyCount--;
            }
            if(s.contains("{")){
                //System.out.println("curlyCount++");
                curlyCount++;
            }
            if(s.contains("}")){
                //System.out.println("curlyCount--");
                curlyCount--;
            }

            if(s.contains("(")){
                //System.out.println("PharCount++");
                pharCount++;
            }
            if(s.contains(")")){
                //System.out.println("PharCount--");
                pharCount--;
            }
            
            if(addBlock){
                ifBlock.put(lineNum, s);
            }

            if(curlyCount == 0 && pharCount == 0){
                addBlock = false;
            }  
        }
        System.out.println();
        for(Map.Entry<Integer, String> get :ifBlock.entrySet()){
            System.out.println(get.getKey() + " // " + get.getValue());
        }
        System.out.println("-------------------------------------------------------");
        skipIF = true;
        if(!ifBlock.isEmpty()){
            scanIF(ifBlock, skipIF);            
        }
        ArrayList<Integer> flag_lineNum = syntaxCheck(ifBlock);
        return flag_lineNum;
    }
}


