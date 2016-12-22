package basicblock;

import java.util.ArrayList;
import java.util.HashMap;

public class BasicBlock {
	public static HashMap<String, Integer> BBMap = new HashMap<>();
	public static ArrayList<GroupBlock> BasicBlockCal(String ucode){
		ArrayList<Instruction> labelUcode = labelAdd(ucode);
		ArrayList<GroupBlock> groupBlock = leaderCal(labelUcode);
		return groupBlock;
	}

	private static ArrayList<GroupBlock> leaderCal(ArrayList<Instruction> labelUcode) {
		ArrayList<GroupBlock> groupBlock =new ArrayList<>();
		ArrayList<String> leader = new ArrayList<>();
		ArrayList<Instruction> tempInstruction = new ArrayList<>();
		leader.add("L0");
		leader.add("L1");
		for(int i = 1 ; i < labelUcode.size();i++){
			Instruction temp = labelUcode.get(i);
			System.out.println(temp);
			if(temp.getFunctionName()!=null && temp.getFunctionName().startsWith("$")){
				leader.add(temp.getLabel());
			}else if(temp.getTargetLabel()!=null){
				int labelVal = Integer.parseInt(temp.getLabel().substring(1, temp.getLabel().length()));
				leader.add("L"+(labelVal+1));
			}
		}
		for(int i = 0 ; i < leader.size() ; i++)
			System.out.println(leader.get(i));
		int k = 0;
		for(int i = 0 ; i < leader.size(); i++){
			int currentIndex = Integer.parseInt(leader.get(i).replaceAll("L", ""));
			int nextIndex;
			if(i == leader.size() - 1){
				nextIndex = labelUcode.size();
			}else{				
				nextIndex = Integer.parseInt(leader.get(i+1).replaceAll("L", ""));
			}
			
			if(currentIndex == nextIndex)
				continue;
			
			tempInstruction = new ArrayList<>();
			for(int j = currentIndex ; j < nextIndex; j++){
				tempInstruction.add(labelUcode.get(j));
				if(labelUcode.get(j).getFunctionName()!=null)
					BBMap.put(labelUcode.get(j).getFunctionName(), k);
			}
			
			groupBlock.add(new GroupBlock(k++, "L"+currentIndex, tempInstruction));
		}
		for(int i = 0 ; i < groupBlock.size() ; i++)
			System.out.println(groupBlock.get(i));
		
		return groupBlock;
	}

	private static ArrayList<Instruction> labelAdd(String ucode) {
		String[] temp = ucode.split("\n");
		ArrayList<Instruction> temp2 = new ArrayList<>();
		for(int i = 0 ; i < temp.length ; i++){
			if(temp[i].startsWith("           ")){
				String trimedStr = temp[i].trim();
				if(trimedStr.startsWith("fjp") || trimedStr.startsWith("tjp") || trimedStr.startsWith("ujp")){
					temp2.add(new Instruction("L"+i,null,trimedStr,trimedStr.substring(4, trimedStr.length())));
				}else{
					temp2.add(new Instruction("L"+i,null,trimedStr,null));
				}
			}else{
				if(temp[i].startsWith("$")||temp[i].startsWith("main"))
					temp2.add(new Instruction("L"+i,temp[i].substring(0, 11).trim(),temp[i].substring(11,temp[i].length()-1),null));
				else
					return temp2;
			}
		}
		return temp2;
	}
}
