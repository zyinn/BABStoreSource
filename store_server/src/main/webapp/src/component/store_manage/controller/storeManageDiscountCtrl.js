import panelBaseDialogCtrl from './../../../common/controller/panelBaseDialogCtrl';

const initCtrl = Symbol('initCtrl');

const dataDefine = {

    discountDialog: [

        { id: "discount1", vm: 'selectedBills', component: 'discountTable', attribute: {} },

        {
            id: "discount2", vm: 'outPrice', component: 'inputLabelDrop', attribute: {
                label: '期望价格', inputFlex: 40, itemSource: [
                    {
                        displayName: '‰', vmValue: "millesimal", regexp: /^(\d+)$|^(\d+\.\d{1,2})$/, procFunc: e => {
                            // 月利率‰ * 12 = 年利率%
                            var result = (+e) * 1.20;
                            if (result) result = result.toFixed(3);
                            return result;
                        }
                    },
                    { displayName: '%', vmValue: "percent", regexp: /^(\d+)$|^(\d+\.\d{1,3})$/, isDefault: true, procFunc: e => e }
                ]
            }
        },
        { id: "discount3", vm: 'counterPartyName', component: 'dropLabel', initDtoMatcher: e => e.conditionName === '交易地点', attribute: { label: '交易地点', inputFlex: 40, displayPath: 'name' } }
    ],

    dto: {
        discount: {

            "price": "outPrice.value",

            "quoteProvinces": {
                "provinceCode": "counterPartyName.code",
                "provinceName": "counterPartyName.name"
            }
        },

        discount_billInfoDiscountDto: {
            "billMedium": "billInfoDto.billMediumType.billMedium",
            "billType": "billInfoDto.billMediumType.billType",
            "dueDate": "billInfoDto.billDueDate",
            "amount": "billInfoDto.amount",              

            "acceptingCompanyName": "billInfoDto.acceptingCompanyName",
            "acceptingCompanyType": "billInfoDto.acceptingCompanyType"
        }
    },     

    validation: [
        { prop: "outPrice", rule: "rangeClose", displayName: "期望价格", errorMessage: "请输入正确的期望价格。", param: { pattern: { min: 0, max: Infinity } } },
        { prop: "outPrice", displayName: "期望价格", errorMessage: "请输入正确的期望价格。‰小数点后2位，%小数点后3位", param: [] },  
    ],

    initVaildRuleFunc: (originRule, dialogDataDefine) => {

        var checkFunctionParam = [vm => {
            // 只校验inputString的内容，校验失败的情况下value的值视为无效。 
            if (!vm || !vm.selectedItem) {
                console.error("checkFunctionParam: !vm || !vm.selectedItem");
                return false;
            }

            if (!vm.selectedItem.regexp || vm.selectedItem.regexp.constructor.name !== "RegExp") {
                console.error('checkFunctionParam: !vm.selectedItem.regexp || vm.selectedItem.regexp.constructor.name !== "RegExp"');
                return false;
            }

            if (vm.inputString + "" === "") return true;

            return vm.selectedItem.regexp.test(vm.inputString);
        }];                                

        // 期望价格格式不正确，输入仅支持数字，‰小数点后2位，%小数点后3位。
        originRule.findItem(e => e.prop === 'outPrice' && !e.rule).param = checkFunctionParam;

        var vaildRules = dialogDataDefine.findWhere(e => e.attribute && e.attribute.isRequired)
            .map(e => {
                return {
                    prop: e.vm,
                    displayName: e.attribute.label,
                    rule: 'required',
                    errorMessage: `${e.attribute.label}值不能为空`,
                }
            });

        vaildRules.push.apply(vaildRules, originRule);

        return vaildRules;
    }
};

class storeManageDiscount extends panelBaseDialogCtrl {
    constructor($sce, commonService, configConsts) {
        super($sce, commonService, configConsts);

        this[initCtrl]();
    };

    [initCtrl]() {
        console.debug('storeManageDiscount initCtrl');

        this.dialogType = "storeManage";
        this.contentStyle = {
            'width': '800px'
        };

        if (this.dtoForLoadingVm) {

            let define = [];
            Array.prototype.push.apply(define, angular.copy(dataDefine.dto.loadOutStoreInfo))
            Array.prototype.push.apply(define, [{
                dtoProp: undefined, vmProp: 'outType', func: (dto, vmDefine) => {
                    let itemSource = this.commonService.getPropertyX(vmDefine, 'attribute.itemSource');
                    if (!(itemSource instanceof Array)) return;
                    let value = itemSource.findItem(e => e.code === dto.outType);
                    this.commonService.setPropertyX(vmDefine, 'attribute.ngModel', value);
                }
            }]);
            this[loadDto](this.dtoForLoadingVm, define);
            this.dtoForLoadingVm = undefined;
        }
    };

    $onDialogVmChanging(result) {
        if (!result) return;
    };

    $onDialogVmChanged(result, prop) {
        if (!result) return;                                              
    };
};

var controllerInjector = ['$sce', 'commonService', 'configConsts', storeManageDiscount];

export { controllerInjector, dataDefine };