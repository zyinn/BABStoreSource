class dataDefineService {
    constructor () {
        this.billTypeParam = {
            "ELE_BKB":{billMedium:"ELE",billType:"BKB"},
            "PAP_BKB":{billMedium:"PAP",billType:"BKB"},
            "ELE_CMB":{billMedium:"ELE",billType:"CMB"},
            "PAP_CMB":{billMedium:"PAP",billType:"CMB"},
        };

        this.acceptingCompanyType ={
            "GG": "国股",
            "CS": "城商",
            "NS": "农商",
            "NX": "农信",
            "NH": "农合",
            "CZ": "村镇",
            "WZ": "外资",
            "CW": "财务公司",
            "YBH": "有保函",
            "WBH": "无保函",
            "CET": "央企",
            "SOE": "国企",
            "LET": "地方性企业",
            "OTH": "其他"
        };

        this.outType = {
            "DSC":"票据贴现",
            "OFK":"卖出票据",
            "PDG":"票据质押",
            "PYB":"应付票据",
            "OTH":"其他"
        };

        this.godownType = {
            "BYI":"买入票据",
            "RCB":"应收票据",
            "SGN":"签发票据",
            "OTH":"其他"
        };

        this.billStatus = {
            "ITS":"已入库",
            "OTS":"已出库",
            "CAL":"已作废"
        };
    };
};

export default dataDefineService;