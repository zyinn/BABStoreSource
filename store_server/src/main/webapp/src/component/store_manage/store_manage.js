import { controllerInjector as storeManageGodownCtrl, dataDefine as godownDataDefine } from './controller/storeManageGodownCtrl';
import { controllerInjector as storeManageOutStoreCtrl, dataDefine as outStoreDataDefine } from './controller/storeManageOutStoreCtrl';
import { controllerInjector as storeManageDiscountCtrl, dataDefine as discountDataDefine } from './controller/storeManageDiscountCtrl';

import quotOperConfirmCtrl from './controller/quotOperConfirm';

const userInfo = Symbol('userInfo');

const initView = Symbol('initView');
const doGodown = Symbol('doGodown');
const doEditBill = Symbol('doEditBill');
const doOutStore = Symbol('doOutStore');
const doDiscount = Symbol('doDiscount');

const openBillDialog = Symbol('openBillDialog');
const openOutStoreDialog = Symbol('openOutStoreDialog');
const openDiscountDialog = Symbol('openDiscountDialog');
const openErrorDialog = Symbol('openErrorDialog');

const vaildVmForDialog = Symbol('vaildVmForDialog');
const getSelectedItemInGrid = Symbol('getSelectedItemInGrid');

const dataDefine = {
    showPanel: {
        "ALL": ["billType", "dateType"],
        "ITS": ["billType", "inType", "dateType"],
        "OTS": ["billType", "outType", "dateType"]
    },

    filterBarDefine: [
        {
            id: "storeBar1", vm: "storeType", component: "barNav", containerClass: "inline-block", bVisiable: true,
            attribute: {
                itemSource: [
                    { name: "全部", code: "ALL" },
                    { name: "库存中", code: "ITS" },
                    { name: "已出库", code: "OTS" },
                ]
            }
        }
    ],

    filterPanelDefine: {
        billType: {
            id: "storePanel1", vm: "billType", conditionName: '票据类型', component: "inputButtonSelector", containerClass: "inline-block",
            default: e => e.code === 'ELE_BKB',
            attribute: { label: "票据类型", hasSelectAllButton: true, displayPath: 'name' }
        },
        inType: {
            id: "storePanel2", vm: "inType", conditionName: '入库类型', component: "inputButtonSelector", containerClass: "inline-block",
            attribute: { label: "入库类型", hasSelectAllButton: true, displayPath: 'name' }
        },
        outType: {
            id: "storePanel3", vm: "outType", conditionName: '出库类型', component: "inputButtonSelector", containerClass: "inline-block",
            attribute: { label: "出库类型", hasSelectAllButton: true, displayPath: 'name' }
        },
        dateType: {
            id: "storePanel4", vm: "dateType", component: "dateSelector",
            attribute: { label: "到期日", flex: { selector: "20", date: "20" } }
        }
    },

    dto: {
        billMediumTypeMap: new Map([
            ["ELE_BKB", { billMedium: 'ELE', billType: 'BKB', name: '电银' }],
            ["PAP_BKB", { billMedium: 'PAP', billType: 'BKB', name: '纸银' }],
            ["ELE_CMB", { billMedium: 'ELE', billType: 'CMB', name: '电商' }],
            ["PAP_CMB", { billMedium: 'PAP', billType: 'CMB', name: '纸商' }],
        ])
    }

};

class storeManageCtrl {
    constructor($scope, $http, $q, userService, commonService, componentCommonService, configConsts, gridExcelImportService, storeManageService, gridStoreDefineService) {
        this.$scope = $scope;
        this.userService = userService;
        this.$http = $http;
        this.currentNavItem = "ALL";
        this.componentCommonService = componentCommonService;
        this.commonService = commonService;

        this.configConsts = configConsts;
        this.gridExcelImportService = gridExcelImportService;
        this.storeManageService = storeManageService;
        this.$q = $q;
        this.gridStoreDefineService = gridStoreDefineService;
        this[initView]();
    };

    [initView]() {
        console.debug('storeManageCtrl initView');

        this.currentNavItem = "ALL";
        this.filterBarItem = dataDefine.filterBarDefine;
        this.filterPanelItem = [];
        this.filterPanelItem.push(dataDefine.filterPanelDefine["billType"]);
        this.filterPanelItem.push(dataDefine.filterPanelDefine["dateType"]);
        this[userInfo] = this.userService.getUserInfo();
    };

    [vaildVmForDialog](viewModel, vaildRules) {

        var vaildResult = true;

        if (!(vaildRules instanceof Array)) return this.$q.resolve(true);

        if (vaildRules instanceof Array) {
            vaildRules.forEach(rule => {
                if (!vaildResult) return;
                vaildResult = this.commonService.checkViewModel(viewModel, rule, () => {
                    this.componentCommonService.openErrorDialog({
                        title: '错误',
                        message: rule.errorMessage,
                        theme: this.theme
                    });
                });
            });
        }

        if (!vaildResult) return this.$q.reject(false);

        return this.$q.resolve(vaildResult);
    };

    [doGodown](event) {
        var def = this.$q.defer();

        this.userService.getToken('bab.store.godown').then(res => {
            let quoteToken = res.result[0];
            let itemSource = angular.copy(godownDataDefine.godownDialog);
            if (!itemSource || !(itemSource instanceof Array)) {
                console.warn('[doGodown](event, item) itemSource is null or empty');
            }

            let dialogParams = {
                itemSource: itemSource,
                quoteToken: quoteToken,
                closeDialogDefer: def,
            };
            this[openBillDialog](event, dialogParams);

        }, res => {
            this.componentCommonService.openErrorDialog({
                title: '错误',
                message: '初始化入库表单失败',
                theme: this.theme
            });

            def.reject(false);
        });

        return def.promise;

    };

    [doEditBill](event, item, closeDialogDefer) {
        this.userService.getToken('bab.store.editBill').then(res => {
            var quoteToken = res.result[0];

            if (item && item.storeStatus === 'OTS') {
                let itemSource = angular.copy(outStoreDataDefine.outStoreDialog);
                if (!itemSource || !(itemSource instanceof Array)) {
                    console.warn('[doEditBill](event, item) itemSource is null or empty');
                    return;
                }

                let dialogParams = {
                    itemSource: itemSource,
                    quoteToken: quoteToken,
                    loadingDto: angular.copy(item),
                    closeDialogDefer: closeDialogDefer
                };

                return this[openOutStoreDialog](event, dialogParams);

            } else {
                let itemSource = angular.copy(godownDataDefine.godownDialog);
                if (!itemSource || !(itemSource instanceof Array)) {
                    console.warn('[doEditBill](event, item) itemSource is null or empty');
                    return;
                }

                let billInfoVm = itemSource.findItem(e => e.vm === 'billInfo');
                this.commonService.setPropertyX(billInfoVm, 'attribute.isDisabled', true);

                let dialogParams = {
                    itemSource: itemSource,
                    quoteToken: quoteToken,
                    loadingDto: angular.copy(item),
                    closeDialogDefer: closeDialogDefer
                };
                return this[openBillDialog](event, dialogParams);
            }

        }, res => {
            this.componentCommonService.openErrorDialog({
                title: '错误',
                message: '初始化编辑表单失败',
                theme: this.theme
            });
            return this.$q.reject(res);
        });
    };

    [openBillDialog](event, dialogParams) {

        if (!dialogParams) {
            console.warn('[openBillDialog](event, dialogParams) dialogParams is null or empty');
            return;
        }

        if (!dialogParams.itemSource) console.warn('[openBillDialog](event, dialogParams) dialogParams.itemSource is null or empty');
        if (!dialogParams.quoteToken) console.warn('[openBillDialog](event, dialogParams) dialogParams.quoteToken is null or empty');

        return this.componentCommonService.openAddPriceDialog(storeManageGodownCtrl, {
            template: require('./../../common/component/template/panel_base_dialog.html'),
            itemSource: dialogParams.itemSource,
            theme: this.theme,
            title: '票据入库',
            event: event,
            dto: dialogParams.loadingDto,
            // button: '.animation-target-enter',
            onClosing: result => {

                let vaildRules = godownDataDefine.initVaildRuleFunc(godownDataDefine.validation, godownDataDefine.godownDialog);

                return this[vaildVmForDialog](result, vaildRules).then(res => {
                    if (res) {
                        let dto = this.commonService.getDto(result, godownDataDefine.dto.godown, NaN);

                        dto.billInfoDtoList = [this.commonService.getDto(result, godownDataDefine.dto.godown_billInfo, NaN)];

                        if (dto.billInfoDtoList instanceof Array) {
                            dto.billInfoDtoList.forEach(e => {
                                angular.merge(e, dataDefine.dto.billMediumTypeMap.get(e.billMediumType));
                                delete e.billMediumType;
                            });
                            dto.quoteToken = dialogParams.quoteToken;
                        }

                        let isUpdate = result.id ? true : false;
                        let errorMessage = result.id ? '更新票据失败' : '票据入库失败';

                        return this.storeManageService.createOrUpdateGoDownStores(dto, isUpdate).then(res => {
                            if (dialogParams.closeDialogDefer) dialogParams.closeDialogDefer.resolve();
                            return this.$q.resolve(true);
                        }, res => {

                            this[openErrorDialog](res, {
                                title: '错误',
                                message: errorMessage,
                                theme: this.theme
                            });

                            return this.$q.reject(false);
                        });

                    } else {
                        return this.$q.reject(false);
                    }
                }, res => this.$q.reject(false));
            },
            onVmChanged: result => {

            }
        });
    };

    [doOutStore](event) {
        var def = this.$q.defer();
        this.userService.getToken('bab.store.outStore').then(res => {
            let quoteToken = res.result[0];
            let itemSource = angular.copy(outStoreDataDefine.outStoreDialog);
            if (!itemSource || !(itemSource instanceof Array)) {
                console.warn('[doOutStore](event) itemSource is null or empty');
                return;
            }

            let dialogParams = {
                itemSource: itemSource,
                quoteToken: quoteToken,
                closeDialogDefer: def,
            };
            this[openOutStoreDialog](event, dialogParams);
        }, res => {
            this.componentCommonService.openErrorDialog({
                title: '错误',
                message: '初始化出库表单失败',
                theme: this.theme
            });

            def.reject(false);
        });

        return def.promise;
    };

    [openOutStoreDialog](event, dialogParams) {
        if (!dialogParams) {
            console.warn('[openOutStoreDialog](event, dialogParams) dialogParams is null or empty');
            return;
        }

        if (!dialogParams.itemSource) console.warn('[openOutStoreDialog](event, dialogParams) dialogParams.itemSource is null or empty');
        if (!dialogParams.quoteToken) console.warn('[openOutStoreDialog](event, dialogParams) dialogParams.quoteToken is null or empty');

        let selectedItems = this[getSelectedItemInGrid](dialogParams);
        if (!selectedItems) return;

        let totalAmount = 0;

        selectedItems.forEach(item => {
            if (!item || !item.billInfoDto) return;
            totalAmount += item.billInfoDto ? (+item.billInfoDto.amount) : 0;
        });

        let selectedBillsVm = dialogParams.itemSource.findItem(e => e.vm === 'selectedBills');
        this.commonService.setPropertyX(selectedBillsVm, 'attribute.itemSource', selectedItems);
        this.commonService.setPropertyX(selectedBillsVm, 'attribute.ngModel', selectedItems.map(e => e.id));

        selectedItems.forEach(item => {
            if (!item || !item.billInfoDto) return;
            item.billInfoDto.billMediumType = dataDefine.dto.billMediumTypeMap.get(`${item.billInfoDto.billMedium}_${item.billInfoDto.billType}`);
        });

        let htmlInfo = dialogParams.itemSource.findItem(e => e.vm === 'htmlInfo');
        if (htmlInfo && htmlInfo.html) htmlInfo.html = htmlInfo.html.format(selectedItems.length, totalAmount);

        this.componentCommonService.openAddPriceDialog(storeManageOutStoreCtrl, {
            template: require('./../../common/component/template/panel_base_dialog.html'),
            itemSource: dialogParams.itemSource,
            theme: this.theme,
            title: '票据出库',
            dto: dialogParams.loadingDto,
            event: event,
            // button: '.animation-target-delivery',
            onClosing: result => {
                let vaildRules = outStoreDataDefine.initVaildRuleFunc(outStoreDataDefine.validation, outStoreDataDefine.outStoreDialog);

                return this[vaildVmForDialog](result, vaildRules).then(res => {
                    if (res) {
                        let dto = this.commonService.getDto(result, outStoreDataDefine.dto.outStore, NaN);
                        dto.quoteToken = dialogParams.quoteToken;

                        return this.storeManageService.createOutStore(dto).then(res => {
                            if (dialogParams.closeDialogDefer) dialogParams.closeDialogDefer.resolve();
                            return this.$q.resolve(true)
                        }, res => {

                            this[openErrorDialog](res, {
                                title: '错误',
                                message: '票据出库失败',
                                theme: this.theme
                            });

                            return this.$q.reject(false);
                        });

                    } else {
                        return this.$q.reject(false);
                    }

                }, res => this.$q.reject(false));

                // return this.$q.reject(false);
            },
            onVmChanged: result => {

            }
        });
    };

    [doDiscount](event) {
        this.userService.getToken('bab.store.doDiscount').then(res => {
            let quoteToken = res.result[0];
            let itemSource = angular.copy(discountDataDefine.discountDialog);
            if (!itemSource || !(itemSource instanceof Array)) {
                console.warn('[doDiscount](event) itemSource is null or empty');
                return;
            }

            let dialogParams = {
                itemSource: itemSource,
                quoteToken: quoteToken
            };
            return this[openDiscountDialog](event, dialogParams);
        }, res => {
            this.componentCommonService.openErrorDialog({
                title: '错误',
                message: '初始化出库表单失败',
                theme: this.theme
            });
        });
    };

    [openDiscountDialog](event, dialogParams) {
        if (!dialogParams) {
            console.warn('[openDiscountDialog](event, dialogParams) dialogParams is null or empty');
            return;
        }

        if (!dialogParams.itemSource) console.warn('[openDiscountDialog](event, dialogParams) dialogParams.itemSource is null or empty');
        if (!dialogParams.quoteToken) console.warn('[openDiscountDialog](event, dialogParams) dialogParams.quoteToken is null or empty');

        let selectedItems = this[getSelectedItemInGrid](dialogParams);
        if (!selectedItems) return;

        selectedItems.forEach(item => {
            if (!item || !item.billInfoDto) return;
            item.billInfoDto.billMediumType = dataDefine.dto.billMediumTypeMap.get(`${item.billInfoDto.billMedium}_${item.billInfoDto.billType}`);
            item.billInfoDto.billDueDateString = item.billInfoDto.billDueDate ? item.billInfoDto.billDueDate.formatDate(this.configConsts.DATE_FORMAT) : '';

            let vm = godownDataDefine.godownDialog.findItem(e => e.vm === "acceptingCompanyType");
            let itemSource = this.commonService.getPropertyX(vm, 'attribute.itemSource');

            if (itemSource instanceof Array) {
                let act = itemSource.findItem(e => e.code === item.billInfoDto.acceptingCompanyType);
                item.billInfoDto.acceptingCompanyString = `${item.billInfoDto.acceptingCompanyName}(${act ? act.name : ''})`;
            }
        });

        let selectedBillsVm = dialogParams.itemSource.findItem(e => e.vm === 'selectedBills');
        this.commonService.setPropertyX(selectedBillsVm, 'attribute.itemSource', selectedItems);
        this.commonService.setPropertyX(selectedBillsVm, 'attribute.ngModel', selectedItems);

        this.componentCommonService.openAddPriceDialog(storeManageDiscountCtrl, {
            template: require('./../../common/component/template/panel_base_dialog.html'),
            itemSource: dialogParams.itemSource,
            theme: this.theme,
            title: '发布贴现',
            // dto: dialogParams.loadingDto,
            event: event,
            // button: '.animation-target-delivery',
            onClosing: result => {

                let vaildRules = discountDataDefine.initVaildRuleFunc(discountDataDefine.validation, discountDataDefine.discountDialog);

                return this[vaildVmForDialog](result, vaildRules).then(res => {
                    if (res) {
                        let dto = result.selectedBills.map(e => {

                            let discountInfo = this.commonService.getDto(result, discountDataDefine.dto.discount, NaN);
                            if (discountInfo.price !== undefined && discountInfo.price === 0) {
                                delete discountInfo.price;
                            }
                            discountInfo.billInfoDiscountDto = this.commonService.getDto(e, discountDataDefine.dto.discount_billInfoDiscountDto, NaN); 
                            return discountInfo;
                        });

                        // let dto = this.commonService.getDto(result, discountDataDefine.dto.discount, NaN);
                        // dto.quoteToken = dialogParams.quoteToken;

                        return this.storeManageService.createDiscount(dto, dialogParams.quoteToken).then(res => {
                            return this.$q.resolve(true)
                        }, res => {

                            this[openErrorDialog](res, {
                                title: '错误',
                                message: '发布贴现失败',
                                theme: this.theme
                            });

                            return this.$q.reject(false);
                        });

                    } else {
                        return this.$q.reject(false);
                    }

                }, res => this.$q.reject(false));

            },
            onVmChanged: result => {

            }
        });
    };

    [getSelectedItemInGrid](dialogParams) {
        let element = angular.element(document.querySelector('grid-store-manage#store_bill_list div[ui-grid]'));

        if (!element) {
            console.warn('[getSelectedItemInGrid]() grid-store-manage#store_bill_list is null or empty');
            return;
        }
        let gridScope = element.scope();
        if (!gridScope) {
            console.warn('[getSelectedItemInGrid]() gridScope is null or empty');
            return;
        }
        if (!gridScope.gridApi) {
            console.warn('[getSelectedItemInGrid]() gridScope.gridApi is null or empty');
            return;
        }
        if (!gridScope.gridApi.selection) {
            console.warn('[getSelectedItemInGrid]() gridScope.gridApi.selection is null or empty');
            return;
        }

        let selectedItems = gridScope.gridApi.selection.getSelectedRows();

        if (!selectedItems || !selectedItems.length || !(selectedItems instanceof Array)) {

            if (dialogParams.loadingDto) {
                selectedItems = [dialogParams.loadingDto];
            } else {
                this.componentCommonService.openErrorDialog({
                    title: '提示',
                    message: '请从列表中选择库存票据，按住Ctrl或Shift可以多选。',
                    theme: this.theme
                });
                return undefined;
            }
        }

        return selectedItems;
    };

    [openErrorDialog](res, defaultOption) {
        let message = '';
        if (res.return_message && res.return_message.errorDetailsList && angular.isArray(res.return_message.errorDetailsList) && res.return_message.errorDetailsList.length !== 0) {
            res.return_message.errorDetailsList.forEach((e, index) => {
                if (index !== res.return_message.errorDetailsList.length - 1) {
                    message += e.detailMsg + '<br>';
                } else {
                    message += e.detailMsg;
                }
            });
        } else if (res.return_message && res.return_message.exceptionCode && res.return_message.exceptionMessage) {
            message = res.return_message.exceptionName + '，' + res.return_message.exceptionMessage;
        }

        if (message) {
            this.componentCommonService.openErrorDialog({
                title: '错误',
                message: message,
                theme: this.theme
            });
        } else {
            this.componentCommonService.openErrorDialog(defaultOption);
        }
    };

    onBtnClickBatDel() {
        let element = angular.element(document.querySelector('grid-store-manage#store_bill_list div[ui-grid]'));
        if (!element) {
            console.warn('[getSelectedItemInGrid]() grid-store-manage#store_bill_list is null or empty');
            return;
        }
        let gridScope = element.scope();
        let sel = gridScope.gridApi.selection.getSelectedRows();
        if (!sel || !angular.isArray(sel)) return;
        if (sel.length === 0) {
            this.componentCommonService.openErrorDialog({
                title: '提示',
                message: '请从列表中选择库存票据，按住Ctrl或Shift可以多选。',
                theme: this.theme
            });
            return;
        }
        this.componentCommonService.openConfirmDialog(quotOperConfirmCtrl, {
            title: "删除确认",
            message: "请再次确认是否批量删除所选报价？",
            quotContent: "",
            theme: this.theme,
            okCallback: e => {
                this.$scope.$broadcast('deleteStore');
            },
            cancelCallback: e => {
            }
        })
    };

    onBtnClickImport() {
        this.gridExcelImportService.openImportDialog({}, this.initData).then(e => {
            this.mdPanelRef = e;
        }, error => {
            console.log('Excel Import Open Dialog Failed.')
        });
    };

    onBtnClickExport() {
        let element = angular.element(document.querySelector('grid-store-manage div[ui-grid]'));

        if (!element) {
            return;
        }
        let gridScope = element.scope();
        if (!gridScope || !gridScope.gridOptions || !gridScope.gridOptions.data || gridScope.gridOptions.data.length <= 0) {
            this.componentCommonService.openErrorDialog({
                title: "提示",
                theme: this.theme,
                message: "库存信息为空！"
            })
            return;
        }

        var param = this.gridStoreDefineService.buildQueryParam({ barParam: this.barParam, panelParam: this.panelParam });
        param.pageNumber = undefined;
        param.pageSize = undefined;
        param.paging = undefined;
        var fullurl = this.configConsts.service_root ? this.configConsts.service_root + this.configConsts.excel_export : this.configConsts.excel_export;
        this.$http({
            url: fullurl,
            method: 'post',
            responseType: "arraybuffer",
            data: JSON.stringify(param),
            headers: this.userService.getAuthHeaders()
        }).success((data, status, headers) => {
            if (status !== 200) {
                this.componentCommonService.openErrorDialog({
                    title: '错误提示',
                    theme: this.theme,
                    message: "导出Excel文件失败"
                });
            }
            var blob = new Blob([data], { type: "application/vnd.ms-excel;charset=UTF-8" });
            var FileSaver = require('file-saver');
            FileSaver.saveAs(blob, "库存信息.xls");
        }).error((data, status) => {
            this.componentCommonService.openErrorDialog({
                title: '错误提示',
                theme: this.theme,
                message: "导出Excel文件失败"
            });
        });
    };

    $routerOnActivate(currentInstruction, previousInstruction) {
        this.theme = this.commonService.getPropertyX(currentInstruction, 'routeData.data.theme');
        if (currentInstruction) {
            if (currentInstruction.routeData && currentInstruction.routeData.data) {
                this.theme = currentInstruction.routeData.data.theme;
            }
        }
    };

    onDelListItem(event, item) {
        var def = this.$q.defer();
        this.componentCommonService.openConfirmDialog(quotOperConfirmCtrl, {
            title: "删除确认",
            message: "请再次确认是否删除该笔价格？",
            quotContent: "",
            theme: this.theme,
            okCallback: e => {
                var lst = [];
                lst.push(item.id);
                def.resolve(this.storeManageService.storeCancel(lst));
            },
            cancelCallback: e => {
                def.reject();
            }
        })

        return def.promise;
    };

    onEditListItem(event, item) {

        let defer = this.$q.defer();

        this[doEditBill](event, item, defer);

        return defer.promise;
    };

    onBtnClick($event, dataDefine) {
        switch (dataDefine.vm) {
            case 'inStore':
                this[doGodown]($event).then(res => {
                    this.$scope.$broadcast("gridRefresh");
                });
                break;
            case 'outStore':
                this[doOutStore]($event).then(res => {
                    this.$scope.$broadcast("gridRefresh");
                });
                break;
            case 'quote':
                this[doDiscount]($event);
                break;
            case 'batDel':
                this.onBtnClickBatDel();
                break;
            case 'import':
                this.onBtnClickImport();
                break;
            case 'export':
                this.onBtnClickExport();
                break;
            default: break;
        }
    };

    onTabChanged(tabCode) {
        if (this.currentNavItem === tabCode) return;
        this.currentNavItem = tabCode;

        this.filterPanelItem = [];
        var panelArray = dataDefine.showPanel[tabCode];
        if (panelArray instanceof Array) {
            panelArray.forEach(e => {
                this.filterPanelItem.push(dataDefine.filterPanelDefine[e]);
            });
        }
        if (!this.filterBar || !angular.isArray(this.filterBar)) return;
        this.filterBar.forEach(e => {
            if (e && e.attribute && e.attribute.ngModel) {
                e.attribute.ngModel = undefined;
            }
        });
    };

    $onInit() {
        this.storeManageService.storeInit().then(res => {
            if (!res || !res.result) return;
            this.initData = res.result;
            var allPanelFilter = [];
            allPanelFilter.push(dataDefine.filterPanelDefine["billType"]);
            allPanelFilter.push(dataDefine.filterPanelDefine["inType"]);
            allPanelFilter.push(dataDefine.filterPanelDefine["outType"]);
            allPanelFilter.push(dataDefine.filterPanelDefine["dateType"]);
            this.filterBar = this.componentCommonService.createSearchCriteriaVm({
                source: res.result,
                dataDefine: allPanelFilter,
                defineMatchFunc: (item, dataDefine) => item && item.conditionName && dataDefine && dataDefine.conditionName && item.conditionName === dataDefine.conditionName
            });

            // 入库DataDefine   
            // 出库DataDefine      
            [
                godownDataDefine.godownDialog,
                outStoreDataDefine.outStoreDialog,
                discountDataDefine.discountDialog
            ].forEach(dialogDefine => {
                dialogDefine.forEach(define => {
                    if (define.initDtoMatcher) {
                        let data = res.result.findItem(e => define.initDtoMatcher(e));
                        if (data.conditions) {
                            if (!data.conditions instanceof Array) return;

                            define.attribute.itemSource = angular.copy(data.conditions);

                            if (define.default) {
                                define.attribute.ngModel = define.attribute.itemSource.findItem(define.default);
                            } else {
                                if (define.attribute.itemSource[0]) define.attribute.itemSource[0].isDefault = true;
                            }
                        }
                    }
                });
            });

            // debugger;

        }, res => {

        })
    };

};

let storeManage = () => {
    return {
        template: require('./template/store_manage.html'),
        bindings: {
            $router: '<'
        },
        controller: ['$scope', '$http', '$q', 'userService', 'commonService', 'componentCommonService', 'configConsts', 'gridExcelImportService', 'storeManageService', 'gridStoreDefineService', storeManageCtrl]
    };
};

export default storeManage;