/**
 * Created by jiannan.niu on 2017/3/16.
 */

import gridExcelImportCtrl from './../controller/gridExcelImportCtrl';

const init = Symbol('init');
const THEME_NAME = "ssAvalonUi";
const getExcelImportPanelTemplate = Symbol('getExcelImportPanelTemplate');

class gridExcelImportService {

    constructor (uiGridConstants, $mdPanel, httpService, userService, configConsts, $q) {
        this.uiGridConstants = uiGridConstants;
        this.httpService = httpService;
        this.userService = userService;
        this.configConsts = configConsts;
        this.$mdPanel = $mdPanel;
        this.$q = $q;

        this.columnDefs = [
            {
                name: '序号',
                width: 50,
                template: '序号',
                enableCellEdit: false,
            },
            {
                name: '票类',
                width: 60,
                template: '票类',
                editableCellTemplate: 'ui-grid/dropdownEditor',
                editDropdownIdLabel: 'code',
                editDropdownValueLabel: 'name',
                editModelField: 'billInfoDtoList[0].billMedium',
                editDropdownOptionsFunction: (rowEntity, colDef) => {
                    let conditions = angular.copy(this.paramEnum.findItem(e => e.conditionName === '票据类型').conditions);
                    let type = conditions.findWhere(e => {
                        return e.code.indexOf(rowEntity.billInfoDtoList[0].billType) !== -1;
                    })
                    type.forEach(e => {
                        e.code = e.code.substring(0, 3);
                    });
                    return type;
                }
            },
            {
                name: '票号',
                width: 320,
                template: '票号',
                editModelField: 'billInfoDtoList[0].billNumber'
            },
            {
                name: '票面金额',
                displayName: '票面金额(元)',
                width: 150,
                template: '票面金额(元)',
                editModelField: 'billInfoDtoList[0].amount',
                type: 'number'
            },
            {
                name: '出票人',
                width: 250,
                template: '出票人',
                editModelField: 'billInfoDtoList[0].drawerName'
            },
            {
                name: '收款人',
                width: 250,
                template: '收款人',
                editModelField: 'billInfoDtoList[0].payeeName'
            },
            {
                name: '承兑人',
                width: 250,
                template: '承兑人',
                editModelField: 'billInfoDtoList[0].acceptingCompanyName'
            },
            {
                name: '承兑人类别',
                width: 100,
                template: '承兑人类别',
                editableCellTemplate: 'ui-grid/dropdownEditor',
                editDropdownIdLabel: 'code',
                editDropdownValueLabel: 'name',
                editModelField: 'billInfoDtoList[0].acceptingCompanyType',
                editDropdownOptionsFunction: (rowEntity, colDef) => {
                    return this.paramEnum.findItem(e => e.conditionName === '承兑人类别').conditions;
                }
            },
            {
                name: '出票日',
                width: 150,
                template: '出票日',
                editModelField: 'billInfoDtoList[0].billStartDate',
                editableCellTemplate: '<md-datepicker ng-model="row.entity.billInfoDtoList[0].billStartDate" md-hide-icons="calendar"></md-datepicker>'
            },
            {
                name: '到期日',
                width: 150,
                template: '到期日',
                editModelField: 'billInfoDtoList[0].billDueDate',
                editableCellTemplate: '<md-datepicker ng-model="row.entity.billInfoDtoList[0].billDueDate" md-hide-icons="calendar"></md-datepicker>'
            },
            {
                name: '调整天数',
                width: 80,
                template: '调整天数',
                editModelField: 'adjustDays',
                type: 'number'
            },
            {
                name: '入库日期',
                width: 150,
                template: '入库日期',
                editModelField: 'godownDate',
                editableCellTemplate: '<md-datepicker ng-model="row.entity.godownDate" md-hide-icons="calendar"></md-datepicker>'
            },
            {
                name: '入库类型',
                width: 120,
                template: '入库类型',
                editableCellTemplate: 'ui-grid/dropdownEditor',
                editDropdownIdLabel: 'code',
                editDropdownValueLabel: 'name',
                editModelField: 'godownType',
                editDropdownOptionsFunction: (rowEntity, colDef) => {
                    return this.paramEnum.findItem(e => e.conditionName === '入库类型').conditions;
                }
            },
            {
                name: '入库价格',
                displayName: '入库价格(%)',
                width: 150,
                template: '入库价格(%)',
                editModelField: 'godownPrice',
                type: 'number'
            },
            {
                name: '操作',
                width: 50,
                template: '操作',
                enableCellEdit: false,
                pinnedRight: true
            },
        ];

        this.templateDefine = new Map([
            ['序号', '<div class="ui-grid-cell-contents text-center">{{ row.entity.index }}</div>'],
            ['票类', '<div class="ui-grid-cell-contents">{{ grid.appScope.$ctrl.gridExcelImportService.paramBillType[row.entity.billInfoDtoList[0].billMedium + "_" + row.entity.billInfoDtoList[0].billType] }}</div>'],
            ['票号', '<div class="ui-grid-cell-contents">{{ row.entity.billInfoDtoList[0].billNumber }}</div>'],
            ['票面金额(元)', '<div class="ui-grid-cell-contents">{{ row.entity.billInfoDtoList[0].amount | numberFilter }}</div>'],
            ['出票人', '<div class="ui-grid-cell-contents">{{ row.entity.billInfoDtoList[0].drawerName }}</div>'],
            ['收款人', '<div class="ui-grid-cell-contents">{{ row.entity.billInfoDtoList[0].payeeName }}</div>'],
            ['承兑人', '<div class="ui-grid-cell-contents">{{ row.entity.billInfoDtoList[0].acceptingCompanyName }}</div>'],
            ['承兑人类别', '<div class="ui-grid-cell-contents">{{ grid.appScope.$ctrl.gridExcelImportService.paramPriceType[row.entity.billInfoDtoList[0].acceptingCompanyType] }}</div>'],
            ['出票日', '<div class="ui-grid-cell-contents" ng-class="{\'warning\': row.entity.billInfoDtoList[0].billStartDate.format(\'yyyy-MM-dd\') === \'1970-01-01\'}">{{ row.entity.billInfoDtoList[0].billStartDate | date:"yyyy-MM-dd" }}</div>'],
            ['到期日', '<div class="ui-grid-cell-contents">{{ row.entity.billInfoDtoList[0].billDueDate | date:"yyyy-MM-dd" }}</div>'],
            ['调整天数', '<div class="ui-grid-cell-contents">{{ row.entity.adjustDays }}</div>'],
            ['入库日期', '<div class="ui-grid-cell-contents">{{ row.entity.godownDate | date:"yyyy-MM-dd" }}</div>'],
            ['入库类型', '<div class="ui-grid-cell-contents">{{ grid.appScope.$ctrl.gridExcelImportService.paramInType[row.entity.godownType] }}</div>'],
            ['入库价格(%)', '<div class="ui-grid-cell-contents">{{ row.entity.godownPrice }}</div>'],
            ['操作', '<div class="ui-grid-cell-contents"><i class="ss-icon ss-icon-delete-normal ss-icon-highlight-bg pointer delete-bg" ng-click="grid.appScope.$ctrl.deleteRow($event, grid.appScope, row.entity.index)"><md-tooltip class="tooltip" md-direction="right">删除</md-tooltip></i></div>']
        ])

        this[init]();
    }

    openImportDialog (params, initData) {

        if (!initData || !angular.isArray(initData) || initData.length === 0) return this.$q.reject();

        this.paramEnum = initData;

        this.paramBillType = this.creatEnum('票据类型', initData);
        this.paramPriceType = this.creatEnum('承兑人类别', initData);
        this.paramInType = this.creatEnum('入库类型', initData);
        this.paramOutType = this.creatEnum('出库类型', initData);

        // console.log(this.paramBillType);
        // console.log(this.paramPriceType);
        // console.log(this.paramInType);
        // console.log(this.paramOutType);

        var position = this.$mdPanel.newPanelPosition()
            .absolute()
            .center();

        var config = {
            attachTo: angular.element(document.body),
            controller: gridExcelImportCtrl,
            controllerAs: '$ctrl',
            bindToController: true,
            panelClass: 'import-panel',
            theme: params.theme || THEME_NAME,
            locals: {
                title: params.title || '批量导入',
                image: params.image || undefined,
                textContent: params.message || "",
                quotContent: params.quotContent || "",
                okText: params.okText || "确认",
                cancelText: params.cancelText || "取消",
                onOkCallback: params.okCallback || (() => true),
                onCancelCallback: params.cancelCallback || (() => true),
                type: params.type
            },
            template: this[getExcelImportPanelTemplate](),
            position: position,
            hasBackdrop: true,
            trapFocus: true,
            fullscreen: false,
            disableParentScroll: false,
            clickOutsideToClose: false
        };

        return this.$mdPanel.open(config);

    }

    buildGridOptions (scope, columnDefs, templateDefine) {
        const uiGridConstants = this.uiGridConstants;
        let options = {
            //行高
            rowHeight: 30,
            //是否显示横向滚动条，只有两个值，默认uiGridConstants.scrollbars.ALWAYS
            enableHorizontalScrollbar: uiGridConstants.scrollbars.ALWAYS,
            enableVerticalScrollbar: uiGridConstants.scrollbars.ALWAYS,
            //是否开启列菜单，默认true
            enableColumnMenus: false,
            //是否显示过滤菜单，默认false
            enableFiltering: false,
            //是否允许排序，默认true
            enableSorting: false,
            enableColumnResizing: false,

            //是否显示grid底边
            // showGridFooter: false,
            //grid底边的高度
            // columnFooterHeight: 50,
            //grid底边的模板
            // gridFooterTemplate: '',
            //是否允许表头全选
            // enableRowHeaderSelection: false,
            //是否允许选取整行
            // enableFullRowSelection: true,

            //是否无限滚动，默认true
            // enableInfiniteScroll: false,
            //滚动到距离底部10条时加载数据，默认20
            // infiniteScrollRowsFromEnd: 20,
            //是否允许向下无限滚动，默认true
            // infiniteScrollDown: false,
            //是否允许向上无限滚动，默认true
            // infiniteScrollUp: false,

            //是否允许固定列
            enablePinning: true,

            //是否允许单元格编辑功能，默认undefined
            enableCellEdit: true,
            enableCellEditOnFocus: true,

            //ui-grid回调函数注册
            onRegisterApi: function (gridApi) {
                scope.gridApi = gridApi;

                gridApi.edit.on.afterCellEdit(scope, (rowEntity, colDef, newValue, oldValue) => {
                    if (typeof newValue === 'string' && newValue.length === 0) {
                        if (colDef.editModelField.includes('[') && colDef.editModelField.includes(']')) {
                            let propArray = colDef.editModelField.split('[')[0];
                            let arrayNumber = colDef.editModelField.split('[')[1].split(']')[0];
                            let prop = colDef.editModelField.split('.')[1];
                            console.log(propArray, arrayNumber, prop)
                            rowEntity[propArray][arrayNumber][prop] = null;
                        } else {
                            rowEntity[colDef.editModelField] = null;
                        }

                    }
                    scope.$ctrl.commonService.safeApply(scope);
                });
            },
            rowTemplate: '<div ng-repeat="col in colContainer.renderedColumns track by col.colDef.name" class="ui-grid-cell" ui-grid-cell></div>',
            columnDefs: this.buildColumn(columnDefs, templateDefine)
        };
        return options;
    }

    columnFactory (define, templateDefine) {
        const uiGridConstants = this.uiGridConstants;
        const sortDefdine = new Map([
            ['', []],
            ['D', [uiGridConstants.DESC]],
            ['A', [uiGridConstants.ASC]],
            ['DA', [uiGridConstants.DESC, uiGridConstants.ASC]],
            ['AD', [uiGridConstants.ASC, uiGridConstants.DESC]],
            ['nDA', [null, uiGridConstants.DESC, uiGridConstants.ASC]],
            ['nAD', [null, uiGridConstants.ASC, uiGridConstants.DESC]]
        ]);

        return {
            name: define.name,
            displayName: define.displayName,
            width: define.width || 100,
            minWidth: define.minWidth || 50,
            maxWidth: define.maxWidth,
            visible: define.visible,
            enableSorting: define.isSort,
            sortDirectionCycle: sortDefdine.get(define.sortDirection),
            headerCellTemplate: define.headerCellTemplate,
            field: define.field,
            sort: define.sort,
            headerCellClass: define.headerCellClass,
            cellTemplate: templateDefine.get(define.template),
            enableCellEdit: define.enableCellEdit,
            type: define.type,
            enableCellEdit: define.enableCellEdit,
            editModelField: define.editModelField,
            editableCellTemplate: define.editableCellTemplate,
            editDropdownOptionsArray: define.editDropdownOptionsArray,
            editDropdownOptionsFunction: define.editDropdownOptionsFunction,
            editDropdownIdLabel: define.editDropdownIdLabel,
            editDropdownValueLabel: define.editDropdownValueLabel,
            pinnedRight: define.pinnedRight,
            pinnedLeft: define.pinnedLeft,
            enablePinning: define.enablePinning

        };
    }

    buildColumn (columnDefs, templateDefine) {
        return columnDefs.map(e => this.columnFactory(e, templateDefine));
    }

    creatEnum (name, param) {
        if (!param || !angular.isArray(param) || param.length === 0) return;
        let item = param.findItem(e => e.conditionName === name);
        if (!item || !item.conditions) return;
        let conditions = item.conditions;
        if (!conditions || !angular.isArray(conditions) || conditions.length === 0) return;
        let enu = {};
        conditions.forEach(e => {
            enu[e.code] = e.name;
        })
        return enu;
    }

    [init] () {
        console.debug('gridExcelImportService initialized.');
    }

    [getExcelImportPanelTemplate] () {
        return require('../component/template/grid_excel_import.html');
    }

}

export default ['uiGridConstants', '$mdPanel', 'httpService', 'userService', 'configConsts', '$q', gridExcelImportService];