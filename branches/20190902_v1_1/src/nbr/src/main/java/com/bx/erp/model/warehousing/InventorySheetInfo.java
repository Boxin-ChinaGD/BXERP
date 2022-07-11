package com.bx.erp.model.warehousing;

import com.bx.erp.model.BaseModel;
import com.bx.erp.model.commodity.Commodity;

public class InventorySheetInfo extends BaseModel {
	private static final long serialVersionUID = 1L;

	@Override
	public String toString() {
		return "InventorySheetInfo [commodity=" + commodity + ", inventoryCommodity=" + inventoryCommodity + "]";
	}

	protected Commodity commodity;
	protected InventorySheet inventorySheet;
	protected InventoryCommodity inventoryCommodity;

	public Commodity getCommodity() {
		return commodity;
	}

	public void setCommodity(Commodity commodity) {
		this.commodity = commodity;
	}

	public InventoryCommodity getInventoryCommodity() {
		return inventoryCommodity;
	}

	public void setInventoryCommodity(InventoryCommodity inventoryCommodity) {
		this.inventoryCommodity = inventoryCommodity;
	}

	public InventorySheet getInventorySheet() {
		return inventorySheet;
	}

	public void setInventorySheet(InventorySheet inventorySheet) {
		this.inventorySheet = inventorySheet;
	}

}
