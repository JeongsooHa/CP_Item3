package basicblock;

import java.util.ArrayList;

public class GroupBlock {
	private int blockIndex;
	private String leaderLabel;
	private ArrayList<Instruction> labelList;
	public GroupBlock(int blockIndex, String leaderLabel, ArrayList<Instruction> labelList) {
		super();
		this.blockIndex = blockIndex;
		this.leaderLabel = leaderLabel;
		this.labelList = labelList;
	}
	public int getBlockIndex() {
		return blockIndex;
	}
	public void setBlockIndex(int blockIndex) {
		this.blockIndex = blockIndex;
	}
	public String getLeaderLabel() {
		return leaderLabel;
	}
	public void setLeaderLabel(String leaderLabel) {
		this.leaderLabel = leaderLabel;
	}
	public ArrayList<Instruction> getLabelList() {
		return labelList;
	}
	public void setLabelList(ArrayList<Instruction> labelList) {
		this.labelList = labelList;
	}
	@Override
	public String toString() {
		return "GroupBlock [blockIndex=" + blockIndex + ", leaderLabel=" + leaderLabel + ", labelList=" + labelList
				+ "]";
	}
	
	
}
