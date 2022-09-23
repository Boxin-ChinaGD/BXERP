package com.bx.erp.sit.sit1.sg.card;

import org.testng.annotations.Test;

import com.bx.erp.test.BaseTestNGSpringContextTest;
import com.bx.erp.test.Shared;

import org.testng.annotations.BeforeClass;

import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.test.context.web.WebAppConfiguration;
import org.testng.annotations.AfterClass;

@WebAppConfiguration
public class CardListTest extends BaseTestNGSpringContextTest {

	protected AtomicInteger order;

	@Test
	public void updateCardStockOfApprovingThenRetrieve() {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CardList_", order, "修改状态为审核中的卡券的库存后查看");

		// ...本地暂时无法实现
	}

	@Test(dependsOnMethods = "updateCardStockOfApprovingThenRetrieve")
	public void updateStockOfTheCardNotPassThenRetrieve() {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CardList_", order, "修改状态为未通过的卡券的库存后查看");

		// ...本地暂时无法实现
	}

	@Test(dependsOnMethods = "updateStockOfTheCardNotPassThenRetrieve")
	public void updateStockOfTheCardToBeLaunchedThenRetrieve() {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CardList_", order, "修改状态为待投放的卡券的库存后查看");

		// ...本地暂时无法实现
	}

	@Test(dependsOnMethods = "updateStockOfTheCardToBeLaunchedThenRetrieve")
	public void updateStockOfTheCardLaunchedThenRetrieve() {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CardList_", order, "修改状态为已投放的卡券的库存后查看");

		// ...本地暂时无法实现
	}

	@Test(dependsOnMethods = "updateStockOfTheCardLaunchedThenRetrieve")
	public void updateStockOfTheCardToBeLaunchedInThePopupWindowThenRetrieve() {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CardList_", order, "修改投放弹窗中状态为待投放的卡券的库存后查看");

		// ...本地暂时无法实现
	}

	@Test(dependsOnMethods = "updateStockOfTheCardToBeLaunchedInThePopupWindowThenRetrieve")
	public void updateStockOfTheLaunchedCardInThePopupWindowThenRetrieve() {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CardList_", order, "修改投放弹窗中状态为已投放且未过期的卡券的库存后查看");

		// ...本地暂时无法实现
	}

	@Test(dependsOnMethods = "updateStockOfTheLaunchedCardInThePopupWindowThenRetrieve")
	public void LaunchCardThenRetrieve() {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CardList_", order, "投放状态为未投放的卡券后查看该卡券");

		// ...本地暂时无法实现
	}

	@Test(dependsOnMethods = "LaunchCardThenRetrieve")
	public void LaunchTheLaunchedCardThenRetrieve() {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CardList_", order, "投放状态为已投放的卡券后查看该卡券");

		// ...本地暂时无法实现
	}

	@Test(dependsOnMethods = "LaunchTheLaunchedCardThenRetrieve")
	public void LaunchCardInThePopupWindowThenRetrieve() {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CardList_", order, "投放状态为未投放的卡券后查看该卡券");

		// ...本地暂时无法实现
	}

	@Test(dependsOnMethods = "LaunchCardInThePopupWindowThenRetrieve")
	public void LaunchTheLaunchedCardInThePopupWindowThenRetrieve() {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CardList_", order, "投放状态为已投放且未过期的卡券后查看该卡券");

		// ...本地暂时无法实现
	}

	@Test(dependsOnMethods = "LaunchTheLaunchedCardInThePopupWindowThenRetrieve")
	public void updateTheStockOfApprovingCardThenDelete() {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CardList_", order, "修改状态为审核中的卡券的库存后删除该卡券");

		// ...本地暂时无法实现
	}

	@Test(dependsOnMethods = "updateTheStockOfApprovingCardThenDelete")
	public void updateTheStockOfTheCardNotPassThenDelete() {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CardList_", order, "修改状态为未通过的卡券的库存后删除该卡券");

		// ...本地暂时无法实现
	}

	@Test(dependsOnMethods = "updateTheStockOfTheCardNotPassThenDelete")
	public void updateTheStockOfTheCardToBeLaunchedThenDelete() {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CardList_", order, "修改状态为待投放的卡券的库存后删除该卡券");

		// ...本地暂时无法实现
	}

	@Test(dependsOnMethods = "updateTheStockOfTheCardToBeLaunchedThenDelete")
	public void updateTheStockOfTheCardLaunchedThenDelete() {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CardList_", order, "修改状态为已投放的卡券的库存后删除该卡券");

		// ...本地暂时无法实现
	}

	@Test(dependsOnMethods = "updateTheStockOfTheCardLaunchedThenDelete")
	public void launchTheCardThenDelete() {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CardList_", order, "投放状态为未投放的卡券后删除该卡券");

		// ...本地暂时无法实现
	}

	@Test(dependsOnMethods = "launchTheCardThenDelete")
	public void launchTheCardAndGetItThenDelete() {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CardList_", order, "投放状态为未投放的卡券并领取后删除该卡券");

		// ...本地暂时无法实现
	}

	@Test(dependsOnMethods = "launchTheCardAndGetItThenDelete")
	public void launchTheLaunchedCardThenDelete() {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CardList_", order, "投放状态为已投放的卡券后删除该卡券");

		// ...本地暂时无法实现
	}

	@Test(dependsOnMethods = "launchTheLaunchedCardThenDelete")
	public void updateTheStockOfCardToBeLaunchedInThePopupWindowThenDelete() {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CardList_", order, "修改投放弹窗中状态为待投放的卡券的库存后删除");

		// ...本地暂时无法实现
	}

	@Test(dependsOnMethods = "updateTheStockOfCardToBeLaunchedInThePopupWindowThenDelete")
	public void updateTheStockOfLaunchedCardInThePopupWindowThenDelete() {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CardList_", order, "修改投放弹窗中状态为已投放且未过期的卡券的库存后删除");

		// ...本地暂时无法实现
	}

	@Test(dependsOnMethods = "updateTheStockOfLaunchedCardInThePopupWindowThenDelete")
	public void launchTheCardInThePopupWindowThenDelete() {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CardList_", order, "投放弹窗中状态为未投放的卡券后删除该卡券");

		// ...本地暂时无法实现
	}

	@Test(dependsOnMethods = "launchTheCardInThePopupWindowThenDelete")
	public void launchTheCardInThePopupWindowAndGetItThenDelete() {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CardList_", order, "投放弹窗中状态为未投放的卡券并领取后删除该卡券");

		// ...本地暂时无法实现
	}

	@Test(dependsOnMethods = "launchTheCardInThePopupWindowAndGetItThenDelete")
	public void launchTheLaunchedCardInThePopupWindowThenDelete() {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CardList_", order, "投放弹窗中状态为已投放的卡券后删除该卡券");

		// ...本地暂时无法实现
	}

	@Test(dependsOnMethods = "launchTheLaunchedCardInThePopupWindowThenDelete")
	public void retrieveTheCardsWhichApprovingThenDelete() {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CardList_", order, "查询状态为审核中的卡券，并删除其中的数据");

		// ...本地暂时无法实现
	}

	@Test(dependsOnMethods = "retrieveTheCardsWhichApprovingThenDelete")
	public void retrieveTheCardsWhichNotPassThenDelete() {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CardList_", order, "查询状态为未通过的卡券，并删除其中的数据");

		// ...本地暂时无法实现
	}

	@Test(dependsOnMethods = "retrieveTheCardsWhichNotPassThenDelete")
	public void retrieveTheCardsWhichToBeLaunchedThenDelete() {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CardList_", order, "查询状态为待投放的卡券，并删除其中的数据");

		// ...本地暂时无法实现
	}

	@Test(dependsOnMethods = "retrieveTheCardsWhichToBeLaunchedThenDelete")
	public void retrieveTheCardsWhichLaunchedThenDelete() {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CardList_", order, "查询状态为已投放的卡券，并删除其中的数据");

		// ...本地暂时无法实现
	}

	@Test(dependsOnMethods = "retrieveTheCardsWhichLaunchedThenDelete")
	public void retrieveTheCardsWhichDeletedThenDelete() {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CardList_", order, "查询状态为已删除的卡券，并删除其中的数据");

		// ...本地暂时无法实现
	}

	@Test(dependsOnMethods = "retrieveTheCardsWhichDeletedThenDelete")
	public void retrieveTheCardsWhichViolatoryThenDelete() {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CardList_", order, "查询状态为违规下架的卡券，并删除其中的数据");

		// ...本地暂时无法实现
	}

	@Test(dependsOnMethods = "retrieveTheCardsWhichViolatoryThenDelete")
	public void createNewShopThenViewData() {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CardList_", order, "新建门店后，查看数据");

	}

	@BeforeClass
	public void beforeClass() {
		Shared.printTestClassStartInfo();

		order = new AtomicInteger();
	}

	@AfterClass
	public void afterClass() {
	}

}
