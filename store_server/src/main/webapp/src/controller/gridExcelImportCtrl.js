/**
 * Created by jiannan.niu on 2017/3/16.
 */

const initView = Symbol('initView');
const THEME_NAME = "ssAvalonUi";
class gridExcelImportCtrl {

    constructor ($scope, $mdDialog, $mdPanel, commonService, gridExcelImportService, configConsts, httpService, userService, componentCommonService, storeManageService) {
        this.$scope = $scope;
        this.$mdDialog = $mdDialog;
        this.$mdPanel = $mdPanel;
        this.commonService = commonService;
        this.gridExcelImportService = gridExcelImportService;
        this.configConsts = configConsts;
        this.httpService = httpService;
        this.userService = userService;
        this.componentCommonService = componentCommonService;
        this.storeManageService = storeManageService;

        this.Validation = {
            'billInfoDtoList[0].billMedium': '票类',
            'billInfoDtoList[0].billType': '票类',
            'billInfoDtoList[0].acceptingCompanyName': '承兑人',
            'billInfoDtoList[0].acceptingCompanyType': '承兑人类别',
            'billInfoDtoList[0].amount': '票面金额',
            'billInfoDtoList[0].billNumber': '票号',
            'godownType': '入库类型',
            'godownPrice': '入库价格'
        };

        this[initView]();
    };


    [initView] () {
        console.debug('gridExcelImportCtrl initView.')

        this.theme = THEME_NAME;

        this.pickerConfig = {
            class: 'datepicker-no-tip',
            readOnly: true,
            yearMax: +new Date().format('yyyy') + 1,
            yearMin: +new Date().format('yyyy'),
            upperRange: new Date().getTime() + 86400000 * this.configConsts.maxQuoteDays,
            lowerRange: new Date().getTime()
        };

        let columnDefs = this.gridExcelImportService.columnDefs;
        let templateDefine = this.gridExcelImportService.templateDefine;
        this.$scope.gridOptions = this.gridExcelImportService.buildGridOptions(this.$scope, columnDefs, templateDefine);
    };

    handleFiles (files) {
        if (!files[0] || !files[0].name) return;
        if (!(/\.(xlsx|xls)$/.test(files[0].name))) {
            this.fileName = '请选择正确的文件格式';
            this.commonService.safeApply(this.$scope);
            return;
        }

        this.fileName = files[0].name;
        this.commonService.safeApply(this.$scope);

        let reader = new FileReader();
        reader.readAsDataURL(files[0]);
        reader.onload = (e) => {
            let param = {data: e.target.result, fileName: files[0].name}
            this.storeManageService.excelImportParser(param).then(
                (res) => {
                    if (res.return_code === 0 && angular.isArray(res.result) && res.result.length === 1 && res.result[0].storeGoDownDtos) {

                        if (angular.isArray(res.result[0].storeGoDownDtos) && res.result[0].storeGoDownDtos.length !== 0) {

                            res.result[0].storeGoDownDtos.forEach(e => {
                                //这里日期为空时会写入1970年1月1日
                                e.godownDate = new Date(e.godownDate);
                                e.billInfoDtoList[0].billDueDate = new Date(e.billInfoDtoList[0].billDueDate);
                                e.billInfoDtoList[0].billStartDate = new Date(e.billInfoDtoList[0].billStartDate);
                            });

                            this.$scope.gridOptions.data = this.addIndex(res.result[0].storeGoDownDtos, 'index');
                        }
                        if (res.result[0].invalids !== null) {
                            let message = '';
                            if (angular.isArray(res.result[0].invalids) && res.result[0].invalids.length !== 0) {
                                res.result[0].invalids.forEach((e, index) => {
                                    if (index !== res.result[0].invalids.length - 1) {
                                        message += e + '<br>';
                                    } else {
                                        message += e;
                                    }
                                });
                            } else {
                                message = '导入出错，请重试！';
                            }
                            this.componentCommonService.openErrorDialog({
                                title: '错误提示',
                                theme: THEME_NAME,
                                message: message
                            });
                        }
                    } else {
                        let message = '';
                        if (angular.isArray(res.result[0].invalids) && res.result[0].invalids.length !== 0) {
                            res.result[0].invalids.forEach((e, index) => {
                                if (index !== res.result[0].invalids.length - 1) {
                                    message += e + '<br>';
                                } else {
                                    message += e;
                                }
                            });
                        } else {
                            message = '导入出错，请重试！';
                        }
                        this.componentCommonService.openErrorDialog({
                            title: '错误提示',
                            theme: THEME_NAME,
                            message: message
                        });
                    }
                }, (err) => {
                    let message = '';
                    if (err.return_message && err.return_message.errorDetailsList && angular.isArray(err.return_message.errorDetailsList) && err.return_message.errorDetailsList.length !== 0) {
                        err.return_message.errorDetailsList.forEach((e, index) => {
                            if (index !== err.return_message.errorDetailsList.length - 1) {
                                message += e.detailMsg + '<br>';
                            } else {
                                message += e.detailMsg;
                            }
                        });
                    } else if (err.return_message && err.return_message.exceptionCode && err.return_message.exceptionMessage) {
                        message = err.return_message.exceptionName + '，' + err.return_message.exceptionMessage;
                    } else {
                        message = '导入出错，请重试！';
                    }
                    this.componentCommonService.openErrorDialog({
                        title: '错误提示',
                        theme: THEME_NAME,
                        message: message
                    });
                }
            )
            $('#file').val('');
        }
    };

    $onClickOk (event) {

        if (!this.$scope.gridOptions.data || !angular.isArray(this.$scope.gridOptions.data)) return;
        if (this.$scope.gridOptions.data.length === 0) {
            this.componentCommonService.openErrorDialog({
                title: '错误提示',
                theme: THEME_NAME,
                message: '请先导入模板文件'
            });
            return;
        }

        this.storeManageService.createOrUpdateGoDownStores(this.$scope.gridOptions.data, false).then(res => {
            // console.log(res);
            if (res.return_code === 0 && res.return_message === "Success") {
                this.$scope.$emit('gridRefresh');
                this.mdPanelRef && this.mdPanelRef.close().then(() => {
                    this.mdPanelRef.destroy();
                });
            }
        }, err => {
            let message = '';
            if (err.return_message && err.return_message.errorDetailsList && angular.isArray(err.return_message.errorDetailsList) && err.return_message.errorDetailsList.length !== 0) {
                err.return_message.errorDetailsList.forEach((e, index) => {
                    let header = e.detailMsg.split(',')[0];
                    let detailMsg = e.detailMsg.substring(header.length+1);
                    if (index !== err.return_message.errorDetailsList.length - 1) {
                        message += header + '，' + (this.Validation[e.sourceFiled] || '') + detailMsg + '<br>';
                    } else {
                        message += header + '，' + (this.Validation[e.sourceFiled] || '') + detailMsg;
                    }
                });
            } else {
                message = '报价出错！';
            }
            this.componentCommonService.openErrorDialog({
                title: '错误提示',
                theme: THEME_NAME,
                message: message
            });
        });
    };

    $onClickCancel () {
        this.mdPanelRef && this.mdPanelRef.close().then(() => {
            this.mdPanelRef.destroy();
        });
    };

    deleteRow ($event, scope, id) {
        if (!scope || !id) return;
        let data = scope.gridOptions.data.findWhere(e => e.index !== id);
        scope.gridOptions.data = this.addIndex(data, 'index');
        this.commonService.safeApply(scope);
    };

    addIndex (data, prop) {
        if (!data || !angular.isArray(data) || !prop || !angular.isString(prop)) return;
        data.forEach((e, index) => {
            e[prop] = index + 1;
        });
        return data;
    };
}

export default ['$scope', '$mdDialog', '$mdPanel', 'commonService', 'gridExcelImportService', 'configConsts', 'httpService', 'userService', 'componentCommonService', 'storeManageService', gridExcelImportCtrl];