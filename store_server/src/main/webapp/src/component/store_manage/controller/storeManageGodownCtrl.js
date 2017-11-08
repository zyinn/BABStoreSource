import panelBaseDialogCtrl from './../../../common/controller/panelBaseDialogCtrl';

const initCtrl = Symbol('initCtrl');
const buildLoadBillInfoDataDefine = Symbol('buildLoadBillInfoDataDefine');
const loadDto = Symbol('loadDto');



const dataDefine = {
    godownDialog: [
        { id: "smEnter1", vm: 'billMediumType', initDtoMatcher: e => e.conditionName === '票据类型', component: 'dropLabel', default: e => e.code === 'ELE_BKB', attribute: { label: '票据类型', displayPath: 'name' } },
        { id: "smEnter2", vm: 'billInfo', component: 'inputSearchStores', attribute: { label: '票号', isRequired: true } },
        {
            id: "smEnter3", vm: 'amount', component: 'inputLabelDrop', attribute: {
                label: '票面金额', isRequired: true, itemSource: [
                    { displayName: '元', regexp: /^(\d+)$|^(\d+\.\d{1,2})$/, procFunc: e => e },
                    {
                        displayName: '万元', isDefault: true, regexp: /^(\d+)$|^(\d+\.\d{1,6})$/, procFunc: e => {
                            var num = (e + "").toString().split('.');
                            var addZero = function padRight(str, length) {
                                if (str.length >= length)
                                    return str;
                                else
                                    return padRight(str + "0", length);
                            };
                            if (num.length === 1) return +(e + "0000");
                            if (num.length != 2) return 0;

                            if (num[1].length > 4) {
                                return num[0] + num[1].substr(0, 4) + '.' + num[1].substr(4, num[1].length - 1);
                            } else {
                                return num[0] + addZero(num[1], 4);
                            }
                        }
                    }
                ]
            }
        },
        { id: "smEnter4", vm: 'drawerName', component: 'inputLabel', attribute: { label: '出票人' } },
        { id: "smEnter5", vm: 'payeeName', component: 'inputLabel', attribute: { label: '收款人' } },
        {
            id: "smEnter6", vm: 'acceptingCompanyType', initDtoMatcher: e => e.conditionName === '承兑人类别', component: 'inputLabelDrop',
            attribute: { label: '承兑人', displayPath: 'name', isRequired: true }
        },
        { id: "smEnter7", vm: 'billStartDate', component: 'inputDatePicker', attribute: { label: '出票日期' } },
        { id: "smEnter8", vm: 'billDueDate', component: 'inputDatePicker', attribute: { label: '到期日期' } },
        {
            id: "smEnter9", vm: 'theRemainingTerm', component: 'inputLabelDrop', attribute: {
                label: '转入剩余期限', isInputDisabled: false, itemSource: [
                    { displayName: '0天', vmValue: 0 },
                    { displayName: '1天', vmValue: 1 },
                    { displayName: '2天', vmValue: 2 },
                    { displayName: '3天', vmValue: 3 },
                    { displayName: '4天', vmValue: 4 },
                    { displayName: '5天', vmValue: 5 },
                    { displayName: '6天', vmValue: 6 },
                    { displayName: '7天', vmValue: 7 },
                    { displayName: '8天', vmValue: 8 },
                    { displayName: '9天', vmValue: 9 },
                    { displayName: '10天', vmValue: 10 },
                ]
            }
        },
        { id: "smEnter10", vm: 'godownDate', component: 'inputDatePicker', attribute: { label: '入库日期', isFixedPopup: true, ngModel: new Date() } },

        {
            id: "smEnter11", vm: 'godownType', initDtoMatcher: e => e.conditionName === '入库类型', component: 'dropLabel', default: e => e.code === 'BYI',
            attribute: { label: '入库类型', displayPath: 'name' }
        },
        {
            id: "smEnter12", vm: 'godownPrice', component: 'inputLabelDrop', attribute: {
                label: '入库价格', itemSource: [
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
        { id: "smEnter13", vm: 'id', component: 'hiddenString', attribute: {} },
        { id: "smEnter14", vm: 'billInfoId', component: 'hiddenString', attribute: {} },
    ],

    editDialog: {
        'billTypeMedium': e => {
            return {
                billType: e && e.billInfoDto ? e.billInfoDto.billType : undefined,
                billMedium: e && e.billInfoDto ? e.billInfoDto.billMedium : undefined
            };
        },
        'billInfo': e => e.direction,
        'amount': e => e && e.amountsPayable ? (e.amountsPayable / 10000) : undefined,
        'payeeName': e => e && e.billInfoDto ? e.billInfoDto.payeeName : undefined,
        'drawerName': e => e && e.billInfoDto ? e => e.billInfoDto.drawerName : undefined,

        //'acceptingCompanyName': e,
        'billStartDate': e => e && e.billInfoDto ? new Date(e.billInfoDto.billStartDate) : undefined,
        'billDueDate': e => e && e.billInfoDto ? new Date(e.billInfoDto.billDueDate) : undefined,
        // 'theRemainingTerm': e,   
        'godownDate': e => e && e.godownDate ? new Date(e.godownDate) : undefined,

        // 'godownType': e,
        'godownPrice': e => e.godownPrice
    },

    dto: {

        godown: {
            "godownType": "godownType.code",
            "godownDate": "godownDate",
            "godownPrice": "godownPrice.value",
            "adjustDays": "theRemainingTerm.selectedItem.vmValue",
            "id": "id"
        },

        godown_billInfo: {
            "billNumber": "billInfo.inputString",
            // "operatorId": "1c497085d6d511e48ec3000c29a13c19",
            //"billMedium": "billMediumType.value.billMedium",
            //"billType": "billMediumType.value.billType",
            "billMediumType": "billMediumType.code",
            "amount": "amount.value",
            "payeeName": "payeeName",
            "drawerName": "drawerName",
            "acceptingCompanyName": "acceptingCompanyType.inputString",
            "acceptingCompanyType": "acceptingCompanyType.selectedItem.code",
            "billStartDate": "billStartDate",
            "billDueDate": "billDueDate",
            // "createDate": "1487671200000",
            "theRemainingTerm": "theRemainingTerm.value",
            // "latestUpdateDate": "1487671200000",
            // "billStatus": "VLD",
            "id": "billInfoId"
        },

        loadGodownInfo: [
            { dtoProp: 'id', vmProp: 'id' },
            { dtoProp: 'billInfoId', vmProp: 'billInfoId' },
            { dtoProp: 'godownPrice', vmProp: 'godownPrice' }
        ],

        loadBillInfo: [
            { dtoProp: 'payeeName', vmProp: 'payeeName' },
            { dtoProp: 'drawerName', vmProp: 'drawerName' },

            { dtoProp: 'billStartDate', vmProp: 'billStartDate' },
            { dtoProp: 'billDueDate', vmProp: 'billDueDate' }
        ]
    },

    validation: [
        { prop: "billInfo.inputString", rule: "regexp", displayName: "票号", errorMessage: "请输入正确的票号。", param: { pattern: /([12]\d{29})$|(\d{6}[56]\d{9})$/ } },
        { prop: "billInfo.inputString", rule: "required", displayName: "票号", errorMessage: "票号不能为空" },
        { prop: "amount", rule: "rangeClose", displayName: "票面金额", errorMessage: "请输入正确的票面金额。", param: { pattern: { min: 0, max: Infinity } } },
        { prop: "godownPrice", rule: "rangeClose", displayName: "入库价格", errorMessage: "请输入正确的入库价格。", param: { pattern: { min: 0, max: Infinity } } },
        { prop: "amount", displayName: "票面金额", errorMessage: "票面金额输入仅支持数字，万元小数点后六位，元小数点后两位。", param: [] },
        { prop: "amount.inputString", rule: "required", displayName: "票面金额", errorMessage: "票面金额不能为空" },
        { prop: "godownPrice", displayName: "入库价格", errorMessage: "请输入正确的入库价格。‰小数点后2位，%小数点后3位", param: [] },

        { prop: "payeeName", rule: "regexp", displayName: "收款人", errorMessage: "收款人仅支持汉字，不超过60字符", param: { pattern: /^[\u4e00-\u9fa5]{1,60}$/ } },
        { prop: "drawerName", rule: "regexp", displayName: "出票人", errorMessage: "出票人仅支持汉字，不超过60字符", param: { pattern: /^[\u4e00-\u9fa5]{1,60}$/ } },
        { prop: "acceptingCompanyType.inputString", rule: "required", displayName: "承兑人", errorMessage: "承兑人不能为空" },
        { prop: "acceptingCompanyType.inputString", rule: "regexp", displayName: "承兑人", errorMessage: "承兑人仅支持汉字，不超过60字符", param: { pattern: /^[\u4e00-\u9fa5]{1,60}$/ } },
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

        // 票面金额格式不正确，输入仅支持数字，万元小数点后六位，元小数点后两位。
        originRule.findItem(e => e.prop === 'amount' && !e.rule).param = checkFunctionParam;

        // 期望价格格式不正确，输入仅支持数字，‰小数点后2位，%小数点后3位。
        originRule.findItem(e => e.prop === 'godownPrice' && !e.rule).param = checkFunctionParam;

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

class storeManageEnterCtrl extends panelBaseDialogCtrl {
    constructor($sce, commonService, configConsts) {
        super($sce, commonService, configConsts);

        this[initCtrl]();
    };

    [initCtrl]() {
        console.debug('storeManageEnterCtrl initCtrl');

        this.dialogType = "storeManage";
        this.contentStyle = {
            'width': '480px'
        };

        if (this.dtoForLoadingVm) {

            let define = [];

            Array.prototype.push.apply(define, angular.copy(dataDefine.dto.loadGodownInfo));
            Array.prototype.push.apply(define, [
                {
                    dtoProp: undefined, vmProp: 'billInfo', func: (dto, vmDefine) => {
                        let newModel = { inputString: this.commonService.getPropertyX(dto, 'billInfoDto.billNumber') };
                        this.commonService.setPropertyX(vmDefine, 'attribute.ngModel', newModel);
                    }
                }, {
                    dtoProp: undefined, vmProp: 'theRemainingTerm', func: (dto, vmDefine) => {
                        let itemSource = this.commonService.getPropertyX(vmDefine, 'attribute.itemSource');
                        let newModel = {
                            inputString: this.commonService.getPropertyX(dto, 'billInfoDto.theRemainingTerm'),
                            selectedItem: itemSource instanceof Array ? itemSource.findItem(e => e.vmValue === dto.adjustDays) : undefined
                        };
                        this.commonService.setPropertyX(vmDefine, 'attribute.ngModel', newModel);
                    }
                }, {
                    dtoProp: undefined, vmProp: 'godownType', func: (dto, vmDefine) => {
                        let itemSource = this.commonService.getPropertyX(vmDefine, 'attribute.itemSource');
                        if (!(itemSource instanceof Array)) return;
                        let value = itemSource.findItem(e => e.code === dto.godownType);
                        this.commonService.setPropertyX(vmDefine, 'attribute.ngModel', value);
                    }
                }, {
                    dtoProp: undefined, vmProp: 'godownDate', func: (dto, vmDefine) => {
                        this.commonService.setPropertyX(vmDefine, 'attribute.ngModel', new Date(dto.godownDate));
                    }
                }
            ]);

            this[loadDto](this.dtoForLoadingVm, define);

            define = this[buildLoadBillInfoDataDefine]();

            this[loadDto](this.dtoForLoadingVm.billInfoDto, define);
            this.dtoForLoadingVm = undefined;
        }
    };

    [buildLoadBillInfoDataDefine]() {

        let define = [];

        Array.prototype.push.apply(define, angular.copy(dataDefine.dto.loadBillInfo));
        Array.prototype.push.apply(define, [
            {
                dtoProp: undefined, vmProp: 'billMediumType', func: (dto, vmDefine) => {
                    let itemSource = this.commonService.getPropertyX(vmDefine, 'attribute.itemSource');
                    if (!(itemSource instanceof Array)) return;
                    let value = itemSource.findItem(e => e.code === `${dto.billMedium}_${dto.billType}`);
                    this.commonService.setPropertyX(vmDefine, 'attribute.ngModel', value);
                }
            },
            {
                dtoProp: undefined, vmProp: 'amount', func: (dto, vmDefine) => {
                    this.commonService.setPropertyX(vmDefine, 'attribute.ngModel', dto && dto.amount ? (dto.amount / 10000) : undefined);
                }
            }, {
                dtoProp: undefined, vmProp: 'acceptingCompanyType', func: (dto, vmDefine) => {
                    let itemSource = this.commonService.getPropertyX(vmDefine, 'attribute.itemSource');
                    let newModel = {
                        inputString: dto.acceptingCompanyName,
                        selectedItem: itemSource instanceof Array ? itemSource.findItem(e => e.code === dto.acceptingCompanyType) : undefined
                    };
                    this.commonService.setPropertyX(vmDefine, 'attribute.ngModel', newModel);
                }
            }, {
                dtoProp: undefined, vmProp: 'theRemainingTerm', func: (dto, vmDefine) => {
                    let oldModel = this.commonService.getPropertyX(vmDefine, 'attribute.ngModel');
                    let newInputString = this.commonService.getPropertyX(dto, 'billInfoDto.theRemainingTerm');
                    if (oldModel && oldModel !== 0) {
                        this.commonService.setPropertyX(vmDefine, 'attribute.ngModel.inputString', newInputString);
                    } else {
                        let itemSource = this.commonService.getPropertyX(vmDefine, 'attribute.itemSource');
                        let newModel = {
                            inputString: newInputString,
                            selectedItem: itemSource instanceof Array ? itemSource.findItem(e => e.vmValue === dto.adjustDays) : undefined
                        };
                        this.commonService.setPropertyX(vmDefine, 'attribute.ngModel', newModel);
                    }
                }
            }
        ]);

        return define;
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

    //计算剩余天数
    getRemainingDays (start, end) {
        let date = new Date(end);
        date.setHours(0);
        date.setMinutes(0);
        date.setSeconds(0);
        date.setMilliseconds(0);
        let now = new Date(start);
        now.setHours(0);
        now.setMinutes(0);
        now.setSeconds(0);
        now.setMilliseconds(0);
        let d = parseInt(date - now) / (24 * 60 * 60 * 1000);
        return d;
    }

    $onDialogVmChanging(result, prop) {

        // console.debug(`$onDialogVmChanging prop:${prop}`);

        if (!result) return;
    };

    $onDialogVmChanged(result, prop) {

        // console.debug(`$onDialogVmChanged prop:${prop}`);

        if (!result) return;

        if (!this.itemSource) return;

        switch (prop) {
            case 'billInfo':
                let selectedItem = this.commonService.getPropertyX(result, 'billInfo.selectedItem');
                if (!selectedItem) return;
                this[loadDto](selectedItem, this[buildLoadBillInfoDataDefine]());
                break;
            case 'godownDate':
            case 'billDueDate':
            case 'theRemainingTerm':

                let startDate = this.commonService.getPropertyX(result, 'godownDate');
                let endDate = this.commonService.getPropertyX(result, 'billDueDate');

                if (startDate && endDate) {
                    let dateSpan = this.getRemainingDays(startDate, endDate);
                    if (dateSpan === '--') return;

                    var vm = this.itemSource.findItem(e => e.vm === 'theRemainingTerm');
                    if (vm) {
                        let seletedTheRemainingTerm = this.commonService.getPropertyX(vm, 'attribute.ngModel.selectedItem.vmValue');
                        if (!seletedTheRemainingTerm) seletedTheRemainingTerm = 0;
                        let value = dateSpan + +seletedTheRemainingTerm;
                        if (!value) value = 0;
                        if (vm.attribute.ngModel && vm.attribute.ngModel.inputString) {
                            let newModel = angular.copy(vm.attribute.ngModel);
                            newModel.inputString = value + '';
                            newModel.value = value;
                            vm.attribute.ngModel = newModel
                        } else {
                            vm.attribute.ngModel = value;
                        }
                    }
                }

                break;
            default: break;
        };

    };
};

var controllerInjector = ['$sce', 'commonService', 'configConsts', storeManageEnterCtrl];

export { controllerInjector, dataDefine };