package edu.macalester.mscs.network;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

public class Test {
	
	public static void main(String[] args){
		//System.out.println("===== Start");
		getData();
		//System.out.println("===== End");
	}
	
	public static int NOISE = 4;
	// threshold: 3=13
	public static int REACH = 20;
	
	public static MatrixAndNames getData(){
    	MatrixAndNames data = null;
    	try {
			//File file = new File("./b4dae.txt");
    		
    		String book = "A Storm of Swords";
    		//File file = new File("D:\\MAC\\2011Spring\\%COMP 124 Object Oriented Programming\\workspace\\graphNetworkAppletGOT3\\bin\\got3test.txt");
			File file = new File("src/main/resources/text/got.txt");
    		
			String text = "";
			Scanner scanner = new Scanner(file);
			while (scanner.hasNext()) {
				text += scanner.next()+" ";
			}
			scanner.close();
			
			//long-list
			String nameInput = CharacterLists.NEW_SHORT_LIST;
			
			//short-list
//			String nameInput = "Eddard=Ned Catelyn Robb Sansa Arya Bran Rickon Jon Benjen Lyanna Theon=Reek Roose Ramsay Hodor Osha Luwin Rodrik " +
//					"Mordane Nan Walton Jojen Meera Jeyne Rickard Daenerys=Dany=Khaleesi Viserys Rhaegar Aegon=Egg Aerys Connington Jorah " +
//					"Brynden=Bloodraven Missandei Daario Worm Rakharo Doreah Irri Mirri Qotho Kraznys Xaro Pyat Quaithe Lysa Sweetrobin Yohn Tywin " +
//					"Cersei Jaime=Kingslayer Joffrey Myrcella Tommen Tyrion=Imp=Halfman Kevan Lancel Bronn Gregor=Mountain Sandor=Hound=Dog " +
//					"Podrick=Pod Shae Amory Hallyne Robert Stannis Selyse Shireen Melisandre Davos Renly Brienne Beric Gendry Anguy Meryn Salladhor " +
//					"Matthos Cressen Balon Asha Euron Victarion Aeron=Damphair Dagmer Doran Arianne Quentyn Trystane Elia Oberyn Ellaria Obara " +
//					"Nymeria Tyene Sarella Elia Obella Dorea Loreza Areo Hoster Edmure Brynden=Blackfish Walder Lothar Marillion Roslin Mace Loras " +
//					"Margaery Olenna Jeor Aemon Yoren Samwell=Sam Janos Alliser Mance Ygritte Gilly Craster Val Dalla Rattleshirt Bowen Eddison=Edd " +
//					"Grenn Rast Pypar Karl Qhorin=Halfhand Lommy Orell Styr Petyr=Littlefinger Varys=Spider Pycelle Barristan Arys Ilyn Qyburn " +
//					"Sparrow Drogo Syrio Jaqen Illyrio Thoros Duncan=Dunk Hizdahr Yezzan Tycho Waif Unella";
			
			//String nameInput = "Bran Jon Ben Theon Eddard=Ned Cersei Robb";
//			String nameInput = "Bran Jon Theon Robb Joffrey Tyrion=Imp Arya Sansa Cersei Jaime Eddard=Ned Gregor Catelyn Robert Rickon Benjen "
//					+"Tywin Samwell=Sam Gendry Shae Varys Bronn Jeor Daenerys=Khaleesi=Dany "
//					+"Jorah Petyr=Littlefinger Sandor Viserys Drogo Lysa Selyse Renly Roose Walder Lothar "
//					+"Balon Kevan Lancel Podrick=Pod Amory Osha Hodor Rickard Luwin Rodrik Mordane Nan Barristan Rakharo Doreah Irri Edmure Brynden "
//					+"Mace Loras Aemon Grenn Rast Pypar Yoren Alliser Bowen Illyrio Mirri Qotho Beric Thoros Anguy Marillion Syrio Myrcella Tommen "
//					+"Pycelle Meryn Janos Dontos Ilyn Mance Davos Stannis Margaery Melisandre Ygritte Talisa Robin Shireen Salladhor Matthos "
//					+"Cressen Ramsay Yara Dagmer Alton Oberyn Brienne Jojen Meera Daario Missandei Kovarro Roslin Olenna Eddison "
//					+"Karl Qhorin Kraznys Xaro Pyat Quaithe Qyburn Jaqen Lommy Hallyne Orell Craster Gilly Styr";
			
			// use this one
//			String nameInput = "Bran Jon Theon Robb Joffrey Tyrion=Imp Arya Sansa Cersei Jaime Eddard=Ned Gregor Catelyn Robert Rickon Benjen "
//					+"Tywin Samwell=Sam Gendry Shae Varys Bronn Daenerys=Khaleesi=Dany "
//					+"Jorah Petyr Sandor Viserys Drogo Lysa Renly Walder Lothar "
//					+"Kevan Podrick=Pod Hodor Rickard Nan Rakharo Irri Edmure "
//					+"Mace Loras Aemon Grenn Alliser Beric Thoros Anguy Marillion Myrcella Tommen "
//					+"Pycelle Meryn Janos Dontos Ilyn Mance Davos Stannis Margaery Melisandre Ygritte Salladhor "
//					+"Oberyn Brienne Jojen Meera Daario Missandei Roslin Olenna "
//					+"Qhorin Kraznys Qyburn Craster Gilly Styr";
			HashMap<String,ArrayList<String>> nickname = new HashMap<>();
			String[] nicknameArr = nameInput.split(" ");
			for(int i=0; i<nicknameArr.length; i++){
				if(nicknameArr[i].contains("=")){
					String[] temp = nicknameArr[i].split("=");
					//System.out.println(temp.length);
					for(int j=1; j<temp.length; j++){
						if(!nickname.containsKey(temp[0])){
							ArrayList<String> others = new ArrayList<>();
							others.add(temp[j]);
							nickname.put(temp[0], others);
						}else{
							ArrayList<String> others = nickname.get(temp[0]);
							others.add(temp[j]);
							nickname.put(temp[0], others);
							//System.out.println(temp[j]);
						}
					}
				}
			}
			String[] nameInputArr = nameInput.split("[ =]");
			int[][] matrix = new int[nameInputArr.length][nameInputArr.length];
			
			String[] input = text.split(" ");
			
			System.out.println("=============================================================");
			System.out.println("=================== PART 1: General Info ====================");
			System.out.println("=============================================================");
			System.out.println("Book: " + book);
			System.out.println("Character names input: ");
			System.out.println(nameInput);
			System.out.println("Proximity check range: " + REACH);
			System.out.println("Noise threshold level: " + NOISE);
			System.out.println();
			System.out.println();
			
			ArrayList<Encounter> edgeWeights = new ArrayList<>();
			
			for(int i=0; i<input.length; i++){
				for(int k=0; k<nameInputArr.length; k++){
					if(input[i].contains(nameInputArr[k])){
						for(int j=Math.max(0, i-REACH); j<=Math.min(input.length-1, i+REACH); j++){
							for(int m=0; m<nameInputArr.length; m++){
								if(m!=k && input[j].contains(nameInputArr[m]) && m>k && i-j != 0
										&& !isNestedNickname(nickname, input[i], nameInputArr[k]) 
										&& !isNestedNickname(nickname, input[j], nameInputArr[m])){
									matrix[k][m]++;
									matrix[m][k]++;
									String context = "";
									for(int contextInd = Math.min(i, j) - 5; contextInd <  Math.max(i, j) + 5; contextInd++){
										try{
											context += input[contextInd] + " ";
										}catch(Exception ignored){}
									}
									edgeWeights.add(new Encounter(nameInputArr[k], nameInputArr[m], i, j, Math.abs(j-i), context));
								}
							}
						}
					}
				}
			}
			
			
			System.out.println("=============================================================");
			System.out.println("================== PART 2: Edge Collection ==================");
			System.out.println("=============================================================");
			System.out.println();
			System.out.println("============ By Character (including alt. names): ===========");
			
			for(int k=0; k<nameInputArr.length; k++){
				System.out.println();
				System.out.println(nameInputArr[k] + ":");
				for(int i = 0; i < edgeWeights.size(); i++){
					if (edgeWeights.get(i).character1.equals(nameInputArr[k])){
						System.out.println(edgeWeights.get(i).toString());
					}
				}
				for(int i = 0; i < edgeWeights.size(); i++){
					if (edgeWeights.get(i).character2.equals(nameInputArr[k])){
						System.out.println(edgeWeights.get(i).toString());
					}
				}
			}
			
			System.out.println();
			System.out.println("============ By Proximity (including alt. names): ===========");
			for(int i = REACH; i > 0; i--){
				System.out.println();
				System.out.println("Proximity = " + i + "-word :");
				for(int j = 0; j < edgeWeights.size(); j++){
					if (edgeWeights.get(j).proximityLen == i){
						System.out.println(edgeWeights.get(j).toString());
					}
				}
			}
			System.out.println();
			System.out.println();
			
			data = new MatrixAndNames(matrix, nameInputArr);
			data = removeNickname(data, nickname);
			//printMatrix(data.getMatrix(), data.getNames());
			matrix = data.getMatrix();
			nameInputArr = data.getNames();
			//printMatrix(matrix, nameInputArr);
			//System.out.println(text);				
			//System.out.println("===== Done collecting raw data");
			
			System.out.println("=============================================================");
			System.out.println("=================== PART 3: Refining Data ===================");
			System.out.println("=============================================================");
			System.out.println();
			data = purgeData(data);
			//System.out.println("===== Done refining data");
			System.out.println();
			System.out.println();
		
			printResultCSV(data);
			//System.out.println("===== Done printing data as CSV");
			
			

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
    	return data;
    }
	
	private static boolean isNestedNickname(HashMap<String,ArrayList<String>> map, String wordInquestion, String chaName){
		if(map.containsKey(wordInquestion)){
			ArrayList<String> nicknameList = map.get(wordInquestion);
			for(int i=0; i<nicknameList.size(); i++){
				if(nicknameList.get(i).equals(chaName)) return true;
			}
		}
		return false;
	}
	
	private static void printResultCSV(MatrixAndNames data){
		System.out.println("=============================================================");
		System.out.println("=================== PART 4: Matrix as CSV ===================");
		System.out.println("=============================================================");
		System.out.println();
		
		System.out.println(joinString(data.getNames()));
		
		for(int i=0; i<data.getMatrix().length; i++){
			String[] row =Arrays.toString(data.getMatrix()[i]).split("[\\[\\]]")[1].split(", ");
			System.out.println(joinString(row));
		}
		
		System.out.println();
		System.out.println("=============================================================");
		System.out.println("========================= End of log ========================");
		System.out.println("=============================================================");
	}
	
	private static String joinString(String[] stringArray){
		StringBuilder sb = new StringBuilder();
		for (String n : stringArray) { 
		    if (sb.length() > 0) sb.append(',');
		    sb.append(n);
		}
		return sb.toString();
	}
    
	private static MatrixAndNames purgeData(MatrixAndNames data) {
		String[] names = data.getNames();
		int[][] matrix = data.getMatrix();
		int nicknameCount = 0;
		for(int i=0; i< names.length; i++){
			if(names[i].contains("void")) nicknameCount++;
		}
		String[] purgedNames = new String[names.length - nicknameCount];
		int[][] purgedMatrix = new int[purgedNames.length][purgedNames.length];
		
		System.out.println("Removed edge(s): ");
		
		int temp = 0;
		for(int j=0; j<names.length; j++){
			if(!names[j].contains("void")){
				purgedNames[temp] = names[j];
				int col = 0;
				for(int k=0; k<matrix.length; k++){
					if(matrix[j][k]>=0){
						//purgedMatrix[temp][col] = Math.max(matrix[j][k]-5, matrix[j][k]/10);
						purgedMatrix[temp][col] = matrix[j][k];
						if(purgedMatrix[temp][col] < NOISE && purgedMatrix[temp][col] > 0)
						{
							System.out.println(names[j] +  ", " + names[k] + ", " + purgedMatrix[temp][col]);
							purgedMatrix[temp][col] = 0;
						}
						col++;
					}
				}
				temp++;
			}
		}
		
		System.out.println();
		
		HashMap<Integer, Integer> singleton = new HashMap<>();
		for(int i=0; i<purgedMatrix.length; i++){
			Boolean single = true;
			for(int j=0; j<purgedMatrix[i].length; j++){
				if(purgedMatrix[i][j] > 0){
					single = false;
					break;
				}
			}
			if(single)singleton.put(i, i);
		}
		String[] purgedNames2 = new String[purgedNames.length-singleton.size()];
		int[][] purgedMatrix2 = new int[purgedNames2.length][purgedNames2.length];
		int namesPos=0;
		System.out.print("Removed singleton(s): ");
		for(int i=0; i<purgedNames.length; i++){
			if(!singleton.containsKey(i)){
				purgedNames2[namesPos] = purgedNames[i];
				namesPos++;
			}else{
				System.out.print(purgedNames[i]+" ");
			}
		}
		System.out.print("\n");
		int m=0;
		int n=0;
		for(int i=0; i<purgedNames.length; i++){
			n=0;
			if(!singleton.containsKey(i)){
				for(int j=0; j<purgedNames.length; j++){
					if(!singleton.containsKey(j)){
						purgedMatrix2[m][n] = purgedMatrix[i][j];
						n++;
					}
				}
				m++;
			}
		}
		//data.setMatrix(purgedMatrix);
		//data.setNames(purgedNames);
		
		data.setMatrix(purgedMatrix2);
		data.setNames(purgedNames2);
		return data;
	}
	
	public static MatrixAndNames removeNickname(MatrixAndNames man, HashMap<String,ArrayList<String>> nickname){
		//System.out.println(nickname.containsKey("Eddard"));
		for(int i=0; i<man.getNames().length; i++){
			if(nickname.containsKey(man.getNames()[i])){
				int j = i+1;
				//System.out.println(man.getNames().length+" man.getNames().length");
				//System.out.println(man.getNames()[j]+" man.getNames()[j]");
				//System.out.println(nickname.containsValue(man.getNames()[j])+" nickname.containsValue(man.getNames()[j])");
				while(j<man.getNames().length && nickname.get(man.getNames()[i]).contains(man.getNames()[j])){
					for(int m=0; m<man.getMatrix().length; m++){
						if(m != j){
							man.getMatrix()[m][i] = man.getMatrix()[m][i] + man.getMatrix()[m][j];
							man.getMatrix()[m][j] = -1;
						}else{
							for(int n=0; n<man.getMatrix().length; n++){
								man.getMatrix()[i][n] = man.getMatrix()[i][n] + man.getMatrix()[j][n];
								man.getMatrix()[j][n] = -1;
							}
						}
					}
					man.getNames()[j] = "void:"+man.getNames()[j];
					man.getMatrix()[i][i] = 0;
					j++;
				}
			}
		}
		return man;
	}

}
