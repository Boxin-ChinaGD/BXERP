Public CommoditySheetName As String
Public VipSheetName As String
Public ProviderSheetName As String

'商品列名
Public barcodeColumn As String
Public commodityNameColumn As String
Public packageUnitColumn As String
Public priceRetailtradeColumn As String
Public commodityProviderNameColumn As String
Public categoryNameColumn As String
Public specificationColumn As String
Public shelfLifeColumn As String
Public purchasingUnitColumn As String
Public brandNameColumn As String
Public priceVIPColumn As String
Public returnDaysColumn As String
Public purchaseFlagColumn As String
Public nOStartColumn As String
Public purchasingPriceStartColumn As String
'会员列名
Public bonusColumn As String
Public vipcardcodeColumn As String
Public mobileColumn As String
Public vipNameColumn As String
Public sexColumn As String
Public birthdayColumn As String
Public lastConsumeDatetimeColumn As String
'供应商列名
Public providerNameColumn As String
Public districtColumn As String
Public addressColumn As String
Public contactNameColumn As String
Public providerMobileColumn As String
'商品常量
Public MIN_LENGTH_Barcode As Integer
Public MAX_LENGTH_Barcode As Integer
Public MAX_LENGTH_CommodityName As Integer
Public MAX_LENGTH_PackageUnit As Integer
Public MAX_LENGTH_PriceRetailtrade As Integer
Public MIN_LENGTH_PriceRetailtrade As Integer
Public MAX_LENGTH_Provider As Integer
Public MAX_LENGTH_CategoryName As Integer
Public MAX_LENGTH_Specification As Integer
Public MAX_LENGTH_PurchasingUnit As Integer
Public MAX_LENGTH_Brand As Integer
Public MIN_LENGTH_PriceVIP As Integer
Public MAX_LENGTH_PriceVIP As Integer
Public MIN_LENGTH_NOStart As Integer
Public MIN_LENGTH_PurchasingPriceStart As Integer
Public MAX_LENGTH_PurchasingPriceStart As Integer
'会员常量
Public MIN_LENGTH_Bonus As Integer
Public MAX_LENGTH_VipCardCode As Integer
Public MAX_LENGTH_Mobile As Integer
Public MIN_LENGTH_VipName As Integer
Public MAX_LENGTH_VipName As Integer
Public MIN_Sex As Integer
Public MAX_Sex As Integer
'供应商常量
Public MAX_LENGTH_ProviderName As Integer
Public MAX_LENGTH_District As Integer
Public MAX_LENGTH_Address As Integer
Public MAX_LENGTH_ContactName As Integer
Public LENGTH_ProviderMobile As Integer
'商品错误码
Public FIELD_ERROR_Barcode As String
Public FIELD_ERROR_BarcodeLength As String
Public FIELD_ERROR_CommodityName As String
Public FIELD_ERROR_CommodityNameRepeat As String
Public FIELD_ERROR_CommodityNameLength As String
Public FIELD_ERROR_PackageUnit As String
Public FIELD_ERROR_PackageUnitLength As String
Public FIELD_ERROR_PriceRetailtrade As String
Public FIELD_ERROR_PriceRetailtradeScope As String
Public FIELD_ERROR_Provider As String
Public FIELD_ERROR_ProviderLength As String
Public FIELD_ERROR_ProviderExist As String
Public FIELD_ERROR_CategoryName As String
Public FIELD_ERROR_CategoryNameLength As String
Public FIELD_ERROR_Specification As String
Public FIELD_ERROR_SpecificationLength As String
Public FIELD_ERROR_PurchasingUnit As String
Public FIELD_ERROR_PurchasingUnitLength As String
Public FIELD_ERROR_Brand As String
Public FIELD_ERROR_BrandLength As String
Public FIELD_ERROR_PriceVIP As String
Public FIELD_ERROR_PriceVIPNO As String
Public FIELD_ERROR_ShelfLife As String
Public FIELD_ERROR_ReturnDays As String
Public FIELD_ERROR_NOStart As String
Public FIELD_ERROR_NOStartScope As String
Public FIELD_ERROR_PurchasingPriceStart As String
Public FIELD_ERROR_PurchasingPriceStartScope As String
'会员错误码
Public FIELD_ERROR_Bonus As String
Public FIELD_ERROR_BonusScope As String
Public FIELD_ERROR_VipCardCode As String
Public FIELD_ERROR_VipCardCodeScope As String
Public FIELD_ERROR_Mobile As String
Public FIELD_ERROR_MobileScope As String
Public FIELD_ERROR_MobileRepeat As String
Public FIELD_ERROR_VipNameScope As String
Public FIELD_ERROR_Sex As String
Public FIELD_ERROR_SexScope As String
Public FIELD_ERROR_ProviderMobile As String
Public FIELD_ERROR_ProviderMobileScope As String
'正则表达式错误码
Public ERROR_MSG_CnEnNum As String
Public ERROR_MSG_CnEnNumSpace As String
Public ERROR_MSG_CommodityName As String


Public DEFAULT_VALUE_ShelfLife As String
Public DEFAULT_VALUE_ReturnDays As String

Public color As Integer

Public objRegEx As Object
Public cnEnNumRegEx As Object
Public cnEnNumSpaceRegEx As Object
Public commodityNameRegEx As Object
Private Sub CommandButton1_Click()
    CommoditySheetName = "商品"
    VipSheetName = "会员"
    ProviderSheetName = "供应商"
    '正则表达式错误码
    ERROR_MSG_CnEnNum = "只允许中文、数字和英文"
    ERROR_MSG_CnEnNumSpace = "只允许中文、空格、数字和英文"
    ERROR_MSG_CommodityName = "只允许以*“”、$#/()（）-—_、中英数值、空格形式出现"
    '
    Set cnEnNumRegEx = CreateObject("vbscript.regexp")
    cnEnNumRegEx.Pattern = "[一-龥a-zA-Z0-9]"
    cnEnNumRegEx.Global = True
    
    Set cnEnNumSpaceRegEx = CreateObject("vbscript.regexp")
    cnEnNumSpaceRegEx.Pattern = "[一-龥a-zA-Z0-9\s]"
    cnEnNumSpaceRegEx.Global = True
    
    Set commodityNameRegEx = CreateObject("vbscript.regexp")
    commodityNameRegEx.Pattern = "[一-龥a-zA-Z0-9-—*“”、$#/()（）\s]"
    commodityNameRegEx.Global = True
    '红色的颜色代码
    color = 3
    '清除所有批注和描红
    Call CommandButton2_Click
    '检查商品表
    Call CheckCommodity
    '检查会员表
    Call CheckVip
    '检查供应商表
    Call checkProvider
    
    Set cnEnNumRegEx = Nothing
    Set cnEnNumSpaceRegEx = Nothing
    Set commodityNameRegEx = Nothing
End Sub

Public Sub CheckCommodity()
    '商品列名
    barcodeColumn = "A"
    commodityNameColumn = "B"
    packageUnitColumn = "C"
    priceRetailtradeColumn = "D"
    commodityProviderNameColumn = "E"
    categoryNameColumn = "F"
    specificationColumn = "G"
    purchasingUnitColumn = "H"
    brandNameColumn = "I"
    priceVIPColumn = "J"
    shelfLifeColumn = "K"
    returnDaysColumn = "L"
    nOStartColumn = "M"
    purchasingPriceStartColumn = "N"
    
    MIN_LENGTH_Barcode = 7
    MAX_LENGTH_Barcode = 64
    MAX_LENGTH_CommodityName = 32
    MAX_LENGTH_PackageUnit = 7
    MIN_LENGTH_PriceRetailtrade = 0
    MAX_LENGTH_PriceRetailtrade = 10000
    MAX_LENGTH_Provider = 32
    MAX_LENGTH_CategoryName = 10
    MAX_LENGTH_Specification = 8
    MAX_LENGTH_PurchasingUnit = 16
    MAX_LENGTH_Brand = 20
    MIN_LENGTH_PriceVIP = 0
    MAX_LENGTH_PriceVIP = 10000
    MIN_LENGTH_NOStart = 0
    MIN_LENGTH_PurchasingPriceStart = 0
    MAX_LENGTH_PurchasingPriceStart = 10000
    
    FIELD_ERROR_Barcode = "条形码不能为空"
    FIELD_ERROR_BarcodeLength = "条形码的长度必须在7到64之间"
    FIELD_ERROR_CommodityName = "商品名称不能为空"
    FIELD_ERROR_CommodityNameRepeat = "商品名称不能重复"
    FIELD_ERROR_CommodityNameLength = "商品名称长度不能大于32"
    FIELD_ERROR_PackageUnit = "包装单位不能为空"
    FIELD_ERROR_PackageUnitLength = "包装单位长度不能大于7"
    FIELD_ERROR_PriceRetailtrade = "零售价不能为空且必须为数字"
    FIELD_ERROR_PriceRetailtradeScope = "零售价必须在0到10000之间"
    FIELD_ERROR_Provider = "供应商不能为空"
    FIELD_ERROR_ProviderLength = "供应商长度不能大于32"
    FIELD_ERROR_ProviderExist = "该供应商还未在供应商工作表中添加"
    FIELD_ERROR_CategoryName = "类别不能为空"
    FIELD_ERROR_CategoryNameLength = "类别长度不能大于10"
    FIELD_ERROR_Specification = "商品规格不能为空"
    FIELD_ERROR_SpecificationLength = "商品规格长度不能大于8"
    FIELD_ERROR_PurchasingUnit = "采购单位不能为空"
    FIELD_ERROR_PurchasingUnitLength = "采购单位长度不能大于16"
    FIELD_ERROR_Brand = "品牌不能为空"
    FIELD_ERROR_BrandLength = "品牌长度不能大于20"
    FIELD_ERROR_PriceVIP = "会员价不能为空且必须为数字"
    FIELD_ERROR_PriceVIPNO = "会员价必须在0到10000之间"
    FIELD_ERROR_ShelfLife = "保质期必须大于0"
    FIELD_ERROR_ReturnDays = "退货天数必须大于0"
    FIELD_ERROR_NOStart = "期初数量不能为空且必须为数字"
    FIELD_ERROR_PurchasingPriceStart = "期初采购价不能为空且必须为数字"
    FIELD_ERROR_NOStartScope = "期初数量必须大于0"
    FIELD_ERROR_PurchasingPriceStartScope = "期初采购价必须在0到10000之间"
    
    DEFAULT_VALUE_ShelfLife = 365
    DEFAULT_VALUE_ReturnDays = 7
           
    Dim START_LINE_NO As Long
    Dim commodityLastRow As Long
    '开始行数
    START_LINE_NO = 4
    '获取商品表所使用的最后一行
    commodityLastRow = Sheets(CommoditySheetName).Cells(Rows.Count, 1).End(xlUp).row
    For i = START_LINE_NO To commodityLastRow
        '检查条形码
        If IsEmpty(Sheets(CommoditySheetName).Range(barcodeColumn & i)) Then
            Sheets(CommoditySheetName).Range(barcodeColumn & i).Interior.ColorIndex = color
            Sheets(CommoditySheetName).Range(barcodeColumn & i).AddComment FIELD_ERROR_Barcode
        ElseIf Len(Sheets(CommoditySheetName).Range(barcodeColumn & i)) < MIN_LENGTH_Barcode Or Len(Sheets(CommoditySheetName).Range(barcodeColumn & i)) > MAX_LENGTH_Barcode Then
            Sheets(CommoditySheetName).Range(barcodeColumn & i).Interior.ColorIndex = color
            Sheets(CommoditySheetName).Range(barcodeColumn & i).AddComment FIELD_ERROR_BarcodeLength
        End If
        
         '检查商品名称
         
        If IsEmpty(Sheets(CommoditySheetName).Range(commodityNameColumn & i)) Then
            Sheets(CommoditySheetName).Range(commodityNameColumn & i).Interior.ColorIndex = color
            Sheets(CommoditySheetName).Range(commodityNameColumn & i).AddComment FIELD_ERROR_CommodityName
        ElseIf WorksheetFunction.CountIf(Sheets(CommoditySheetName).Range(commodityNameColumn & ":" & commodityNameColumn), Sheets(CommoditySheetName).Range(commodityNameColumn & i)) > 1 Then
            Sheets(CommoditySheetName).Range(commodityNameColumn & i).Interior.ColorIndex = color
            Sheets(CommoditySheetName).Range(commodityNameColumn & i).AddComment FIELD_ERROR_CommodityNameRepeat
        ElseIf Len(Sheets(CommoditySheetName).Range(commodityNameColumn & i)) > MAX_LENGTH_CommodityName Then
            Sheets(CommoditySheetName).Range(commodityNameColumn & i).Interior.ColorIndex = color
            Sheets(CommoditySheetName).Range(commodityNameColumn & i).AddComment FIELD_ERROR_CommodityNameLength
        End If
        checkCommodityName (i)
        
         ' 检查包装单位
        If IsNumeric(Sheets(CommoditySheetName).Range(packageUnitColumn & i)) Then
            Sheets(CommoditySheetName).Range(packageUnitColumn & i).Interior.ColorIndex = color
            Sheets(CommoditySheetName).Range(packageUnitColumn & i).AddComment FIELD_ERROR_PackageUnit
        ElseIf Len(Sheets(CommoditySheetName).Range(packageUnitColumn & i)) > MAX_LENGTH_PackageUnit Then
            Sheets(CommoditySheetName).Range(packageUnitColumn & i).Interior.ColorIndex = color
            Sheets(CommoditySheetName).Range(packageUnitColumn & i).AddComment FIELD_ERROR_PackageUnitLength
        End If
       checkPackageUnitName (i)
        
         '检查零售价
        If IsEmpty(Sheets(CommoditySheetName).Range(priceRetailtradeColumn & i)) Or Not IsNumeric(Sheets(CommoditySheetName).Range(priceRetailtradeColumn & i)) Then
            Sheets(CommoditySheetName).Range(priceRetailtradeColumn & i).Interior.ColorIndex = color
            Sheets(CommoditySheetName).Range(priceRetailtradeColumn & i).AddComment FIELD_ERROR_PriceRetailtrade
        ElseIf Sheets(CommoditySheetName).Range(priceRetailtradeColumn & i) < MIN_LENGTH_PriceRetailtrade Or Sheets(1).Range(priceRetailtradeColumn & i) > MAX_LENGTH_PriceRetailtrade Then
            Sheets(CommoditySheetName).Range(priceRetailtradeColumn & i).Interior.ColorIndex = color
            Sheets(CommoditySheetName).Range(priceRetailtradeColumn & i).AddComment FIELD_ERROR_PriceRetailtradeScope
        End If
        
         '检查供应商名称
        If IsEmpty(Sheets(CommoditySheetName).Range(commodityProviderNameColumn & i)) Then
            Sheets(CommoditySheetName).Range(commodityProviderNameColumn & i).Interior.ColorIndex = color
            Sheets(CommoditySheetName).Range(commodityProviderNameColumn & i).AddComment FIELD_ERROR_Provider
        ElseIf Len(Sheets(CommoditySheetName).Range(commodityProviderNameColumn & i)) > MAX_LENGTH_Provider Then
            Sheets(CommoditySheetName).Range(commodityProviderNameColumn & i).Interior.ColorIndex = color
            Sheets(CommoditySheetName).Range(commodityProviderNameColumn & i).AddComment FIELD_ERROR_ProviderLength
        End If
         '在商品工作表中出现的供应商名称，必须也在供应商表出现
         checkCommodityProvider (i)
         
        
        '检查类别
        If IsEmpty(Sheets(CommoditySheetName).Range(categoryNameColumn & i)) Then
            Sheets(CommoditySheetName).Range(categoryNameColumn & i).Interior.ColorIndex = color
            Sheets(CommoditySheetName).Range(categoryNameColumn & i).AddComment FIELD_ERROR_CategoryName
        ElseIf Len(Sheets(CommoditySheetName).Range(categoryNameColumn & i)) > MAX_LENGTH_CategoryName Then
            Sheets(CommoditySheetName).Range(categoryNameColumn & i).Interior.ColorIndex = color
            Sheets(CommoditySheetName).Range(categoryNameColumn & i).AddComment FIELD_ERROR_CategoryNameLength
        End If
        checkCategoryName (i)
        
        '检查规格
        If IsEmpty(Sheets(CommoditySheetName).Range(specificationColumn & i)) Then
            Sheets(CommoditySheetName).Range(specificationColumn & i).Interior.ColorIndex = color
            Sheets(CommoditySheetName).Range(specificationColumn & i).AddComment FIELD_ERROR_Specification
        ElseIf Len(Sheets(CommoditySheetName).Range(specificationColumn & i)) > MAX_LENGTH_Specification Then
            Sheets(CommoditySheetName).Range(specificationColumn & i).Interior.ColorIndex = color
            Sheets(CommoditySheetName).Range(specificationColumn & i).AddComment FIELD_ERROR_SpecificationLength
        End If
        checkSpecification (i)
        
        
        '检查品牌
        If IsEmpty(Sheets(CommoditySheetName).Range(brandNameColumn & i)) Then
            Sheets(CommoditySheetName).Range(brandNameColumn & i).Interior.ColorIndex = color
            Sheets(CommoditySheetName).Range(brandNameColumn & i).AddComment FIELD_ERROR_Brand
        ElseIf Len(Sheets(CommoditySheetName).Range(brandNameColumn & i)) > MAX_LENGTH_Brand Then
            Sheets(CommoditySheetName).Range(brandNameColumn & i).Interior.ColorIndex = color
            Sheets(CommoditySheetName).Range(brandNameColumn & i).AddComment FIELD_ERROR_BrandLength
        End If
        checkBrand (i)
        
        '检查会员价
        If IsEmpty(Sheets(CommoditySheetName).Range(priceVIPColumn & i)) Or Not IsNumeric(Sheets(CommoditySheetName).Range(priceVIPColumn & i)) Then
            Sheets(CommoditySheetName).Range(priceVIPColumn & i).Interior.ColorIndex = color
            Sheets(CommoditySheetName).Range(priceVIPColumn & i).AddComment FIELD_ERROR_PriceVIP
        ElseIf Sheets(CommoditySheetName).Range(priceVIPColumn & i) < MIN_LENGTH_PriceVIP Or Sheets(1).Range(priceVIPColumn & i) > MAX_LENGTH_PriceVIP Then
            Sheets(CommoditySheetName).Range(priceVIPColumn & i).Interior.ColorIndex = color
            Sheets(CommoditySheetName).Range(priceVIPColumn & i).AddComment FIELD_ERROR_PriceVIPNO
        End If
        
        '检查保质期
        If IsEmpty(Sheets(CommoditySheetName).Range(shelfLifeColumn & i)) Or Not IsNumeric(Sheets(CommoditySheetName).Range(shelfLifeColumn & i)) Then
            Sheets(CommoditySheetName).Range(shelfLifeColumn & i) = DEFAULT_VALUE_ShelfLife
        ElseIf Sheets(CommoditySheetName).Range(shelfLifeColumn & i) < 0 Then
            Sheets(CommoditySheetName).Range(shelfLifeColumn & i).Interior.ColorIndex = color
            Sheets(CommoditySheetName).Range(shelfLifeColumn & i).AddComment FIELD_ERROR_ShelfLife
        End If
        
        '退货天数
        If IsEmpty(Sheets(CommoditySheetName).Range(returnDaysColumn & i)) Or Not IsNumeric(Sheets(CommoditySheetName).Range(returnDaysColumn & i)) Then
            Sheets(CommoditySheetName).Range(returnDaysColumn & i) = DEFAULT_VALUE_ReturnDays
        ElseIf Sheets(CommoditySheetName).Range(returnDaysColumn & i) < 0 Then
            Sheets(CommoditySheetName).Range(returnDaysColumn & i).Interior.ColorIndex = color
            Sheets(CommoditySheetName).Range(returnDaysColumn & i).AddComment FIELD_ERROR_ReturnDays
        End If
        
        '期初数量和期初采购价
        If IsEmpty(Sheets(CommoditySheetName).Range(nOStartColumn & i)) Or Not IsNumeric(Sheets(CommoditySheetName).Range(nOStartColumn & i)) Then
            Sheets(CommoditySheetName).Range(nOStartColumn & i).Interior.ColorIndex = color
            Sheets(CommoditySheetName).Range(nOStartColumn & i).AddComment FIELD_ERROR_NOStart
        ElseIf IsEmpty(Sheets(CommoditySheetName).Range(purchasingPriceStartColumn & i)) Or Not IsNumeric(Sheets(CommoditySheetName).Range(purchasingPriceStartColumn & i)) Then
            Sheets(CommoditySheetName).Range(purchasingPriceStartColumn & i).Interior.ColorIndex = color
            Sheets(CommoditySheetName).Range(purchasingPriceStartColumn & i).AddComment FIELD_ERROR_PurchasingPriceStart
        ElseIf Sheets(CommoditySheetName).Range(nOStartColumn & i) <> -1 Or Sheets(CommoditySheetName).Range(purchasingPriceStartColumn & i) <> -1 Then
            If Sheets(CommoditySheetName).Range(nOStartColumn & i) < MIN_LENGTH_NOStart Then
                Sheets(CommoditySheetName).Range(nOStartColumn & i).Interior.ColorIndex = color
                Sheets(CommoditySheetName).Range(nOStartColumn & i).AddComment FIELD_ERROR_NOStartScope
            End If
            
            If Sheets(CommoditySheetName).Range(purchasingPriceStartColumn & i) < MIN_LENGTH_PurchasingPriceStart Or Sheets(CommoditySheetName).Range(purchasingPriceStartColumn & i) > MAX_LENGTH_PurchasingPriceStart Then
                Sheets(CommoditySheetName).Range(purchasingPriceStartColumn & i).Interior.ColorIndex = color
                Sheets(CommoditySheetName).Range(purchasingPriceStartColumn & i).AddComment FIELD_ERROR_PurchasingPriceStartScope
            End If
            
            '检查采购单位
            If IsEmpty(Sheets(CommoditySheetName).Range(purchasingUnitColumn & i)) Then
                Sheets(CommoditySheetName).Range(purchasingUnitColumn & i).Interior.ColorIndex = color
                Sheets(CommoditySheetName).Range(purchasingUnitColumn & i).AddComment FIELD_ERROR_PurchasingUnit
            ElseIf Len(Sheets(CommoditySheetName).Range(purchasingUnitColumn & i)) > MAX_LENGTH_PurchasingUnit Then
                Sheets(CommoditySheetName).Range(purchasingUnitColumn & i).Interior.ColorIndex = color
                Sheets(CommoditySheetName).Range(purchasingUnitColumn & i).AddComment FIELD_ERROR_PurchasingUnitLength
            End If
        End If
    Next
End Sub

Public Sub CheckVip()
    '会员列名
    bonusColumn = "A"
    mobileColumn = "B"
    vipNameColumn = "C"
    sexColumn = "D"
    birthdayColumn = "E"
    lastConsumeDatetimeColumn = "F"
    
    
    MIN_LENGTH_Bonus = 0
    MAX_LENGTH_VipCardCode = 16
    LENGTH_Mobile = 11
    MIN_LENGTH_VipName = 1
    MAX_LENGTH_VipName = 32
    MIN_Sex = 0
    MAX_Sex = 2
    
    
    FIELD_ERROR_Bonus = "积分不能为空且必须为数字"
    FIELD_ERROR_BonusScope = "积分必须大于或等于0"
    FIELD_ERROR_VipCardCode = "会员卡号不能为空"
    FIELD_ERROR_VipCardCodeScope = "会员卡号必须为16位"
    FIELD_ERROR_Mobile = "手机号码不能为空"
    FIELD_ERROR_MobileScope = "手机号码必须为11位"
    FIELD_ERROR_MobileRepeat = "手机号码不能重复"
    FIELD_ERROR_VipNameScope = "会员名称长度必须在1到32之间"
    FIELD_ERROR_Sex = "性别性别不能为空"
    FIELD_ERROR_SexScope = "性别必须在0到2之间"
    
    Dim vipLastRow As Long
    '获取会员工作表中已使用区域最后一行的行号
    vipLastRow = Sheets(VipSheetName).Cells(Rows.Count, 1).End(xlUp).row
    '开始行数
    START_LINE_NO = 4
     '遍历行
    For i = START_LINE_NO To vipLastRow
        '检查积分
        If IsEmpty(Sheets(VipSheetName).Range(bonusColumn & i)) Or Not IsNumeric(Sheets(VipSheetName).Range(bonusColumn & i)) Then
            Sheets(VipSheetName).Range(bonusColumn & i).Interior.ColorIndex = color
            Sheets(VipSheetName).Range(bonusColumn & i).AddComment FIELD_ERROR_Bonus
        ElseIf Sheets(VipSheetName).Range(bonusColumn & i) < MIN_LENGTH_Bonus Then
            Sheets(VipSheetName).Range(bonusColumn & i).Interior.ColorIndex = color
            Sheets(VipSheetName).Range(bonusColumn & i).AddComment FIELD_ERROR_BonusScope
        End If
        
        '检查手机号码
        If IsEmpty(Sheets(VipSheetName).Range(mobileColumn & i)) Or Not IsNumeric(Sheets(VipSheetName).Range(mobileColumn & i)) Then
            Sheets(VipSheetName).Range(mobileColumn & i).Interior.ColorIndex = color
            Sheets(VipSheetName).Range(mobileColumn & i).AddComment FIELD_ERROR_Mobile
        ElseIf WorksheetFunction.CountIf(Sheets(VipSheetName).Range(mobileColumn & ":" & mobileColumn), Sheets(VipSheetName).Range(mobileColumn & i)) > 1 Then
            Sheets(VipSheetName).Range(mobileColumn & i).Interior.ColorIndex = color
            Sheets(VipSheetName).Range(mobileColumn & i).AddComment FIELD_ERROR_MobileRepeat
        ElseIf Len(Sheets(VipSheetName).Range(mobileColumn & i)) <> LENGTH_Mobile Then
            Sheets(VipSheetName).Range(mobileColumn & i).Interior.ColorIndex = color
            Sheets(VipSheetName).Range(mobileColumn & i).AddComment FIELD_ERROR_MobileScope
        End If
        
        '检查会员名称
        If IsEmpty(Sheets(VipSheetName).Range(vipNameColumn & i)) Then
            Sheets(VipSheetName).Range(vipNameColumn & i) = "NULL"
        ElseIf Len(Sheets(VipSheetName).Range(vipNameColumn & i)) < MIN_LENGTH_VipName Or Len(Sheets(VipSheetName).Range(vipNameColumn & i)) > MAX_LENGTH_VipName Then
            Sheets(VipSheetName).Range(vipNameColumn & i).Interior.ColorIndex = color
            Sheets(VipSheetName).Range(vipNameColumn & i).AddComment FIELD_ERROR_VipNameScope
        End If
        
        '检查性别
        If IsEmpty(Sheets(VipSheetName).Range(sexColumn & i)) Or Not IsNumeric(Sheets(VipSheetName).Range(sexColumn & i)) Then
            Sheets(VipSheetName).Range(sexColumn & i).Interior.ColorIndex = color
            Sheets(VipSheetName).Range(sexColumn & i).AddComment FIELD_ERROR_Sex
        ElseIf Sheets(VipSheetName).Range(sexColumn & i) < MIN_Sex Or Sheets(VipSheetName).Range(sexColumn & i) > MAX_Sex Then
            Sheets(VipSheetName).Range(sexColumn & i).Interior.ColorIndex = color
            Sheets(VipSheetName).Range(sexColumn & i).AddComment FIELD_ERROR_SexScope
        End If
            
    Next i
End Sub
Public Sub checkProvider()
    providerNameColumn = "A"
    districtColumn = "B"
    addressColumn = "C"
    contactNameColumn = "D"
    providerMobileColumn = "E"
    
    FIELD_ERROR_ProviderName = "供应商名称不能为空"
    FIELD_ERROR_ProviderNameLength = "供应商名称长度不能大于32"
    FIELD_ERROR_ProviderNameRepeat = "供应商名称不能重复"
    FIELD_ERROR_District = "供应商区域不能为空"
    FIELD_ERROR_DistrictLength = "供应商区域长度不能大于20"
    FIELD_ERROR_Address = "供应商地址不能为空"
    FIELD_ERROR_AddressLength = "供应商地址长度不能大于50"
    FIELD_ERROR_ContactName = "供应商联系人名字不能为空"
    FIELD_ERROR_ContactNameLength = "供应商联系人名字长度不能大于20"
    FIELD_ERROR_ProviderMobile = "手机号码不能为空且必须为数字"
    FIELD_ERROR_ProviderMobileScope = "手机号码必须为11位"
    FIELD_ERROR_ProviderMobileRepeat = "手机号码不能重复"
    
    MAX_LENGTH_ProviderName = 32
    MAX_LENGTH_District = 20
    MAX_LENGTH_Address = 50
    MAX_LENGTH_ContactName = 20
    LENGTH_ProviderMobile = 11
    
    Dim providerLastRow As Long
    '获取供应商工作表中已使用区域最后一行的行号
    providerLastRow = Sheets(ProviderSheetName).Cells(Rows.Count, 1).End(xlUp).row
    '开始行数
    START_LINE_NO = 4
    For i = START_LINE_NO To providerLastRow
         '检查供应商名称
        If IsEmpty(Sheets(ProviderSheetName).Range(providerNameColumn & i)) Then
            Sheets(ProviderSheetName).Range(providerNameColumn & i).Interior.ColorIndex = color
            Sheets(ProviderSheetName).Range(providerNameColumn & i).AddComment FIELD_ERROR_ProviderName
        ElseIf WorksheetFunction.CountIf(Sheets(ProviderSheetName).Range(providerNameColumn & ":" & providerNameColumn), Sheets(ProviderSheetName).Range(providerNameColumn & i)) > 1 Then
            Sheets(ProviderSheetName).Range(providerNameColumn & i).Interior.ColorIndex = color
            Sheets(ProviderSheetName).Range(providerNameColumn & i).AddComment FIELD_ERROR_ProviderNameRepeat
        ElseIf Len(Sheets(ProviderSheetName).Range(providerNameColumn & i)) > MAX_LENGTH_ProviderName Then
            Sheets(ProviderSheetName).Range(providerNameColumn & i).Interior.ColorIndex = color
            Sheets(ProviderSheetName).Range(providerNameColumn & i).AddComment FIELD_ERROR_ProviderNameLength
        End If
        checkProviderName (i)
        
         '检查供应商区域
        If IsEmpty(Sheets(ProviderSheetName).Range(districtColumn & i)) Then
            Sheets(ProviderSheetName).Range(districtColumn & i).Interior.ColorIndex = color
            Sheets(ProviderSheetName).Range(districtColumn & i).AddComment FIELD_ERROR_District
        ElseIf Len(Sheets(ProviderSheetName).Range(districtColumn & i)) > MAX_LENGTH_District Then
            Sheets(ProviderSheetName).Range(districtColumn & i).Interior.ColorIndex = color
            Sheets(ProviderSheetName).Range(districtColumn & i).AddComment FIELD_ERROR_DistrictLength
        End If
        
         '检查供应商地址
        If IsEmpty(Sheets(ProviderSheetName).Range(addressColumn & i)) Then
            Sheets(ProviderSheetName).Range(addressColumn & i).Interior.ColorIndex = color
            Sheets(ProviderSheetName).Range(addressColumn & i).AddComment FIELD_ERROR_Address
        ElseIf Len(Sheets(ProviderSheetName).Range(addressColumn & i)) > MAX_LENGTH_Address Then
            Sheets(ProviderSheetName).Range(addressColumn & i).Interior.ColorIndex = color
            Sheets(ProviderSheetName).Range(addressColumn & i).AddComment FIELD_ERROR_AddressLength
        End If
        checkAddress (i)
        
         '检查供应商联系人
        If IsEmpty(Sheets(ProviderSheetName).Range(contactNameColumn & i)) Then
            Sheets(ProviderSheetName).Range(contactNameColumn & i).Interior.ColorIndex = color
            Sheets(ProviderSheetName).Range(contactNameColumn & i).AddComment FIELD_ERROR_ContactName
        ElseIf Len(Sheets(ProviderSheetName).Range(contactNameColumn & i)) > MAX_LENGTH_ContactName Then
            Sheets(ProviderSheetName).Range(contactNameColumn & i).Interior.ColorIndex = color
            Sheets(ProviderSheetName).Range(contactNameColumn & i).AddComment FIELD_ERROR_ContactNameLength
        End If
        checkContactName (i)
        
         '检查手机号码
        If IsEmpty(Sheets(ProviderSheetName).Range(providerMobileColumn & i)) Or Not IsNumeric(Sheets(ProviderSheetName).Range(providerMobileColumn & i)) Then
            Sheets(ProviderSheetName).Range(providerMobileColumn & i).Interior.ColorIndex = color
            Sheets(ProviderSheetName).Range(providerMobileColumn & i).AddComment FIELD_ERROR_ProviderMobile
        ElseIf WorksheetFunction.CountIf(Sheets(ProviderSheetName).Range(providerMobileColumn & ":" & providerMobileColumn), Sheets(ProviderSheetName).Range(providerMobileColumn & i)) > 1 Then
            Sheets(ProviderSheetName).Range(providerMobileColumn & i).Interior.ColorIndex = color
            Sheets(ProviderSheetName).Range(providerMobileColumn & i).AddComment FIELD_ERROR_ProviderMobileRepeat
        ElseIf Len(Sheets(ProviderSheetName).Range(providerMobileColumn & i)) <> LENGTH_ProviderMobile Then
            Sheets(ProviderSheetName).Range(providerMobileColumn & i).Interior.ColorIndex = color
            Sheets(ProviderSheetName).Range(providerMobileColumn & i).AddComment FIELD_ERROR_ProviderMobileScope
        End If
    Next i
    
End Sub

Public Sub checkCommodityName(ByRef row As Integer)
    Set objMH = commodityNameRegEx.Execute(Sheets(CommoditySheetName).Range(commodityNameColumn & row))

    If objMH.Count <> Len(Sheets(CommoditySheetName).Range(commodityNameColumn & row)) Then
        Sheets(CommoditySheetName).Range(commodityNameColumn & row).Interior.ColorIndex = color
        Sheets(CommoditySheetName).Range(commodityNameColumn & row).AddComment ERROR_MSG_CommodityName
    End If
End Sub

Public Sub checkPackageUnitName(ByRef row As Integer)
    Set objMH = cnEnNumSpaceRegEx.Execute(Sheets(CommoditySheetName).Range(packageUnitColumn & row))

    If objMH.Count <> Len(Sheets(CommoditySheetName).Range(packageUnitColumn & row)) Then
        Sheets(CommoditySheetName).Range(packageUnitColumn & row).Interior.ColorIndex = color
        Sheets(CommoditySheetName).Range(packageUnitColumn & row).AddComment ERROR_MSG_CnEnNumSpace
    End If
End Sub

Public Sub checkProviderName(ByRef row As Integer)
    Set objMH = cnEnNumRegEx.Execute(Sheets(ProviderSheetName).Range(providerNameColumn & row))

    If objMH.Count <> Len(Sheets(ProviderSheetName).Range(providerNameColumn & row)) Then
        Sheets(ProviderSheetName).Range(providerNameColumn & row).Interior.ColorIndex = color
        Sheets(ProviderSheetName).Range(providerNameColumn & row).AddComment ERROR_MSG_CnEnNum
    End If
End Sub

Public Sub checkCategoryName(ByRef row As Integer)
    Set objMH = cnEnNumRegEx.Execute(Sheets(CommoditySheetName).Range(categoryNameColumn & row))

    If objMH.Count <> Len(Sheets(CommoditySheetName).Range(categoryNameColumn & row)) Then
        Sheets(CommoditySheetName).Range(categoryNameColumn & row).Interior.ColorIndex = color
        Sheets(CommoditySheetName).Range(categoryNameColumn & row).AddComment ERROR_MSG_CnEnNum
    End If
End Sub

Public Sub checkSpecification(ByRef row As Integer)
    Set objMH = cnEnNumSpaceRegEx.Execute(Sheets(CommoditySheetName).Range(specificationColumn & row))

    If objMH.Count <> Len(Sheets(CommoditySheetName).Range(specificationColumn & row)) Then
        Sheets(CommoditySheetName).Range(specificationColumn & row).Interior.ColorIndex = color
        Sheets(CommoditySheetName).Range(specificationColumn & row).AddComment ERROR_MSG_CnEnNumSpace
    End If
End Sub

Public Sub checkBrand(ByRef row As Integer)
    Set objMH = cnEnNumRegEx.Execute(Sheets(CommoditySheetName).Range(brandNameColumn & row))

    If objMH.Count <> Len(Sheets(CommoditySheetName).Range(brandNameColumn & row)) Then
        Sheets(CommoditySheetName).Range(brandNameColumn & row).Interior.ColorIndex = color
        Sheets(CommoditySheetName).Range(brandNameColumn & row).AddComment ERROR_MSG_CnEnNum
    End If
End Sub

Public Sub checkAddress(ByRef row As Integer)
    Set objMH = cnEnNumRegEx.Execute(Sheets(ProviderSheetName).Range(addressColumn & row))

    If objMH.Count <> Len(Sheets(ProviderSheetName).Range(addressColumn & row)) Then
        Sheets(ProviderSheetName).Range(addressColumn & row).Interior.ColorIndex = color
        Sheets(ProviderSheetName).Range(addressColumn & row).AddComment ERROR_MSG_CnEnNum
    End If
End Sub

Public Sub checkContactName(ByRef row As Integer)
    Set objMH = cnEnNumRegEx.Execute(Sheets(ProviderSheetName).Range(contactNameColumn & row))

    If objMH.Count <> Len(Sheets(ProviderSheetName).Range(contactNameColumn & row)) Then
        Sheets(ProviderSheetName).Range(contactNameColumn & row).Interior.ColorIndex = color
        Sheets(ProviderSheetName).Range(contactNameColumn & row).AddComment ERROR_MSG_CnEnNum
    End If
End Sub

Public Sub checkCommodityProvider(ByRef row As Integer)
    Dim providerLastRow As Long
    Dim providerName As String
    Dim isProviderExist As Boolean
    '获取供应商工作表中已使用区域最后一行的行号
    providerLastRow = Sheets(ProviderSheetName).Cells(Rows.Count, 1).End(xlUp).row
    '开始行数
    START_LINE_NO = 4
    providerNameColumn = "A"
    For i = START_LINE_NO To providerLastRow
        providerName = Sheets(ProviderSheetName).Range(providerNameColumn & i)
        
        If Sheets(CommoditySheetName).Range(commodityProviderNameColumn & row) = providerName Then
            isProviderExist = True
            Exit For
        End If
    Next i

    If Not isProviderExist Then
        Sheets(CommoditySheetName).Range(commodityProviderNameColumn & row).Interior.ColorIndex = color
        Sheets(CommoditySheetName).Range(commodityProviderNameColumn & row).AddComment FIELD_ERROR_ProviderExist
    End If
End Sub


Private Sub CommandButton2_Click()
    CommoditySheetName = "商品"
    VipSheetName = "会员"
    ProviderSheetName = "供应商"
    
    vipLastRow = Sheets(VipSheetName).Cells(Rows.Count, 1).End(xlUp).row
    vipLastColumn = "G"
    '开始行数
    START_LINE_NO = 4
    If vipLastRow >= START_LINE_NO Then
        Sheets(VipSheetName).Range("A" & START_LINE_NO & ":" & vipLastColumn & vipLastRow).ClearFormats
        Sheets(VipSheetName).Range("A" & START_LINE_NO & ":" & vipLastColumn & vipLastRow).ClearComments
    End If
    
    vipLastColumn = "N"
    commodityLastRow = Sheets(CommoditySheetName).Cells(Rows.Count, 1).End(xlUp).row
    '开始行数
    START_LINE_NO = 4
    If commodityLastRow >= START_LINE_NO Then
        Sheets(CommoditySheetName).Range("A" & START_LINE_NO & ":" & vipLastColumn & commodityLastRow).ClearFormats
        Sheets(CommoditySheetName).Range("A" & START_LINE_NO & ":" & vipLastColumn & commodityLastRow).ClearComments
    End If
    
    providerLastColumn = "E"
    providerLastRow = Sheets(ProviderSheetName).Cells(Rows.Count, 1).End(xlUp).row
    '开始行数
    START_LINE_NO = 4
    If providerLastRow >= START_LINE_NO Then
        Sheets(ProviderSheetName).Range("A" & START_LINE_NO & ":" & providerLastColumn & providerLastRow).ClearFormats
        Sheets(ProviderSheetName).Range("A" & START_LINE_NO & ":" & providerLastColumn & providerLastRow).ClearComments
    End If
End Sub
