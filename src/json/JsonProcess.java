package json;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import basicblock.GroupBlock;
import basicblock.Instruction;
import basicblock.BasicBlock;
public class JsonProcess {
	public static JSONObject makeJsonObject(ArrayList<GroupBlock> list){
		JSONObject jsonObject = new JSONObject();
		
		jsonObject.put("linkFromPortIdProperty", "fromPort");
		jsonObject.put("linkToPortIdProperty", "toPort");
		jsonObject.put("class", "go.GraphLinksModel");
		
		/***** nodeDataArray *****/
		JSONArray nodeDataArray = new JSONArray();
		JSONObject blockInfo = new JSONObject();
		blockInfo.put("category", "Comment");
		blockInfo.put("text", "HMJS");
		blockInfo.put("key", -13);
		nodeDataArray.add(blockInfo);
		
		blockInfo=new JSONObject();
		blockInfo.put("key", -1);
		blockInfo.put("category", "Start");
		//blockInfo.put("loc", "175 0");
		blockInfo.put("text", "Start");
		nodeDataArray.add(blockInfo);
		
		for(int i = 0 ; i < list.size() ; i++){
			blockInfo = new JSONObject();
			blockInfo.put("key", i);
			blockInfo.put("text", "BB"+i);
			nodeDataArray.add(blockInfo);
		}
		blockInfo=new JSONObject();
		blockInfo.put("key", -2);
		blockInfo.put("category", "End");
		//blockInfo.put("loc", "175 640");
		blockInfo.put("text", "End");
		nodeDataArray.add(blockInfo);
		
		jsonObject.put("nodeDataArray", nodeDataArray);
		/****************************/
		
		/***** linkDataArray *****/
		JSONArray linkDataArray = new JSONArray();

		for(int i = -1 ; i < 1 ; i++){
			if(list.size() < 3 && i == 1){
				break;
			}
			blockInfo = new JSONObject();
			blockInfo.put("from", i);
			blockInfo.put("to", i+1);
			blockInfo.put("fromPort", "B");
			blockInfo.put("toPort", "T");
			linkDataArray.add(blockInfo);
		}
		int i =1;
		for(i = 1; i < list.size() ; i++){
			GroupBlock temp = list.get(i);
			Instruction insTemp = temp.getLabelList().get(temp.getLabelList().size()-1);
			if(i == list.size()-1)
				break;
			if(insTemp.getTargetLabel() == null){
				blockInfo = new JSONObject();
				blockInfo.put("from", i);
				blockInfo.put("to", i+1);
				blockInfo.put("fromPort", "B");
				blockInfo.put("toPort", "T");
				linkDataArray.add(blockInfo);
			}else{
				int key = BasicBlock.BBMap.get(insTemp.getTargetLabel());
				
				if(insTemp.getCode().startsWith("ujp")){
					blockInfo = new JSONObject();
					blockInfo.put("from", i);
					blockInfo.put("to", key);
					blockInfo.put("fromPort", "R");
					blockInfo.put("toPort", "R");
					linkDataArray.add(blockInfo);
				}else{				
					blockInfo = new JSONObject();
					blockInfo.put("from", i);
					blockInfo.put("to", key);
					blockInfo.put("fromPort", "B");
					blockInfo.put("toPort", "T");	
					linkDataArray.add(blockInfo);
				}
				
				if(!insTemp.getCode().startsWith("ujp")){
					blockInfo = new JSONObject();
					blockInfo.put("from", i);
					blockInfo.put("to", i+1);
					blockInfo.put("fromPort", "B");
					blockInfo.put("toPort", "T");
					linkDataArray.add(blockInfo);
				}
			}
			
		}
		if(list.size() < 3)
			i = 1;

		blockInfo = new JSONObject();
		blockInfo.put("from", i);
		blockInfo.put("to", -2);
		blockInfo.put("fromPort", "B");
		blockInfo.put("toPort", "T");
		linkDataArray.add(blockInfo);
		
		jsonObject.put("linkDataArray", linkDataArray);
		/****************************/
		System.out.println(jsonObject.toJSONString());
		return jsonObject;
	}
}
