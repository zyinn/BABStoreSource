import panelBaseDialogCtrl from './../../../common/controller/panelBaseDialogCtrl';

const initCtrl = Symbol('initCtrl');
const loadDto = Symbol('loadDto');

const dataDefine = {
    outStoreDialog: [

        { id: "outStore1", vm: 'selectedBills', component: 'outStoreTable', attribute: {} },
        {
            id: "outStore2", vm: 'outType', initDtoMatcher: e => e.conditionName === '出库类型', default: e => e.code === 'DSC', component: 'dropLabel',
            attribute: { label: '出库类型', isRequired: true, displayPath: 'name', inputFlex: 60 }
        },
        { id: "outStore3", vm: 'outDate', component: 'inputDatePicker', attribute: { label: '出库日期', isRequired: true, inputFlex: 60, ngModel: new Date() } },
        {
            id: "outStore4", vm: 'outPrice', component: 'inputLabelDrop', attribute: {
                label: '出库价格', inputFlex: 60, itemSource: [
                    {
                        displayName: '‰', vmValue: "millesimal", regexp: /^(\d+)$|^(\d+\.\d{1,2})$/, procFunc: e => {
                            // 月利率‰ * 12 = 年利率%
                            var result = (+e) * 1.20;
                            if (result) result = result.toFixed(3);
                            return result;
                        }
                    },
                    { displayName: '%', vmValue: "percent", regexp: /^(\d+)$|^(\d+\.\d{1,3})$/, isDefault: true, procFunc: e => +e }
                ]
            }
        },
        { id: "outStore5", vm: 'counterPartyName', component: 'inputLabel', attribute: { label: '交易对手', inputFlex: 60 } },
        {
            id: "outStore5", vm: 'htmlInfo', component: 'labelHtmlContent',
            html: '<label-html-content>\
                       <span class="high-light">{0}</span>\
                        张票面金额合计\
                        <span class="high-light">{1}</span>\
                        元\
                    </label-html-content>',
            attribute: {}
        },
    ],

    dto: {
        outStore: {
            "ids": "selectedBills",
            "outDate": "outDate",
            "outType": "outType.code",
            "outPrice": "outPrice.value",
            "counterPartyName": "counterPartyName"
        },

        loadOutStoreInfo: [


            { dtoProp: 'outDate', vmProp: 'outDate' },
            { dtoProp: 'outPrice', vmProp: 'outPrice' },
            { dtoProp: 'counterPartyName', vmProp: 'counterPartyName' }
        ]
    },

    validation: [
        { prop: "selectedBills.length", rule: "rangeClose", displayName: "票号", errorMessage: "没有选择要出库的票据。", param: { pattern: { min: 0, max: Infinity } } },
        { prop: "outPrice", rule: "rangeClose", displayName: "入库价格", errorMessage: "请输入正确的出库价格。", param: { pattern: { min: 0, max: Infinity } } },

        { prop: "counterPartyName", rule: "regexp", displayName: "交易对手", errorMessage: "交易对手仅支持汉字，不超过60字符", param: { pattern: /^[\u4e00-\u9fa5]{1,60}$/ } },
    ],

    initVaildRuleFunc: (originRule, dialogDataDefine) => {

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

class storeManageOutStoreCtrl extends panelBaseDialogCtrl {
    constructor($sce, commonService, configConsts) {
        super($sce, commonService, configConsts);

        this[initCtrl]();
    };

    [initCtrl]() {
        console.debug('storeManageOutStoreCtrl initCtrl');

        this.dialogType = "storeManage";
        this.contentStyle = {
            'width': '640px'
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

    [loadDto](dto, define) {
        define.forEach(item => {
            let vmDefine = this.itemSource.findItem(e => e.vm === item.vmProp);
            if (!vmDefine) return;

            if (item.dtoProp) {
                this.commonService.setPropertyX(vmDefine, 'attribute.ngModel', dto[item.dtoProp]);
            } else {
                if (item.func) {
                    item.func(dto, vmDefine);
                    // this.commonService.setPropertyX(vmDefine, 'attribute.ngModel', item.func(dto, vmDefine));
                }
            }

        });
    };

    $onDialogVmChanging(result, prop) {
        if (!result) return;

        if (!this.itemSource) return;
    };

    $onDialogVmChanged(result, prop) {
        if (!result) return;

        if (!this.itemSource) return;
    };
};

var controllerInjector = ['$sce', 'commonService', 'configConsts', storeManageOutStoreCtrl];

export { controllerInjector, dataDefine };