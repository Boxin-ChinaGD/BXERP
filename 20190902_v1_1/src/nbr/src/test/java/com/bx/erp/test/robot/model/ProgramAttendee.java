package com.bx.erp.test.robot.model;

import java.util.List;

import com.bx.erp.model.BaseModel;

/** 活动参与者 */
public class ProgramAttendee extends BaseModel {
	private static final long serialVersionUID = 3796369169508359209L;
	
	protected int programUnitID;
	protected int nbrProgramUnitID;
	protected int pos1ProgramUnitID;
	protected int pos2ProgramUnitID;
	protected int pos3ProgramUnitID;
	
	protected List<String> nbrMachineMealInfo;
	protected List<String> pos1MachineMealInfo;
	protected List<String> pos2MachineMealInfo;
	protected List<String> pos3MachineMealInfo;

	public int getProgramUnitID() {
		return programUnitID;
	}

	public void setProgramUnitID(int programUnitID) {
		this.programUnitID = programUnitID;
	}

	public int getNbrProgramUnitID() {
		return nbrProgramUnitID;
	}

	public void setNbrProgramUnitID(int nbrProgramUnitID) {
		this.nbrProgramUnitID = nbrProgramUnitID;
	}

	public int getPos1ProgramUnitID() {
		return pos1ProgramUnitID;
	}

	public void setPos1ProgramUnitID(int pos1ProgramUnitID) {
		this.pos1ProgramUnitID = pos1ProgramUnitID;
	}

	public int getPos2ProgramUnitID() {
		return pos2ProgramUnitID;
	}

	public void setPos2ProgramUnitID(int pos2ProgramUnitID) {
		this.pos2ProgramUnitID = pos2ProgramUnitID;
	}

	public int getPos3ProgramUnitID() {
		return pos3ProgramUnitID;
	}

	public void setPos3ProgramUnitID(int pos3ProgramUnitID) {
		this.pos3ProgramUnitID = pos3ProgramUnitID;
	}

	public List<String> getNbrMachineMealInfo() {
		return nbrMachineMealInfo;
	}

	public void setNbrMachineMealInfo(List<String> nbrMachineMealInfo) {
		this.nbrMachineMealInfo = nbrMachineMealInfo;
	}

	public List<String> getPos1MachineMealInfo() {
		return pos1MachineMealInfo;
	}

	public void setPos1MachineMealInfo(List<String> pos1MachineMealInfo) {
		this.pos1MachineMealInfo = pos1MachineMealInfo;
	}

	public List<String> getPos2MachineMealInfo() {
		return pos2MachineMealInfo;
	}

	public void setPos2MachineMealInfo(List<String> pos2MachineMealInfo) {
		this.pos2MachineMealInfo = pos2MachineMealInfo;
	}

	public List<String> getPos3MachineMealInfo() {
		return pos3MachineMealInfo;
	}

	public void setPos3MachineMealInfo(List<String> pos3MachineMealInfo) {
		this.pos3MachineMealInfo = pos3MachineMealInfo;
	}
}
