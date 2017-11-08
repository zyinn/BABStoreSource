const converColWith = Symbol('converColWith');
const getHeaderTemplate = Symbol('getHeaderTemplate');
const getGridColumnPanelTemplate = Symbol('getGridColumnPanelTemplate');
const THEME_NAME = "ssAvalonUi";
import gridColumnPanelCtrl from '../controller/gridColumnPanelCtrl';

class gridStoreDefineService {

    constructor (uiGridConstants,$interval,httpService,dataDefineService,storeManageService, $mdPanel) {
        this.uiGridConstants = uiGridConstants;
        this.$interval = $interval;
        this.httpService = httpService;
        this.dataDefineService = dataDefineService;
        this.storeManageService = storeManageService;
        this.$mdPanel = $mdPanel;
        this.columsDef = {
            "col_outdate":{name:'col_1',displayName:'出库日期',template:'col_outdate'},
            "col_billtype":{name:'col_2',displayName:'票类',template:'col_billtype'},
            "col_billnumber":{name:'col_3',displayName:'票号',template:'col_billnumber'},
            "col_amount":{name:'col_4',displayName:'票面金额',template:'col_amount'},
            "col_drawername":{name:'col_5',displayName:'出票人',template:'col_drawername'},
            "col_payeename":{name:'col_6',displayName:'收款人',template:'col_payeename'},
            "col_acceptingcompanyname":{name:'col_7',displayName:'承兑人',template:'col_acceptingcompanyname'},
            "col_acceptingcompanytype":{name:'col_8',displayName:'承兑人类别',template:'col_acceptingcompanytype'},
            "col_billstartdate":{name:'col_9',displayName:'出票日',template:'col_billstartdate'},
            "col_billduedate":{name:'col_10',displayName:'到期日',template:'col_billduedate'},
            "col_outcompany":{name:'col_11',displayName:'转出方',template:'col_outcompany'},
            "col_outprice":{name:'col_12',displayName:'出库价格(%)',template:'col_outprice'},
            "col_outtype":{name:'col_13',displayName:'出库类型',template:'col_outtype'},
            "col_oper":{name:'col_14',displayName:'操作',template:'col_oper'},
            "col_godowndate":{name:'col_15',displayName:'入库日期',template:'col_godowndate'},
            // "col_remainday":{name:'col_16',displayName:'剩余期限',template:'col_remainday'},
            "col_godowntype":{name:'col_17',displayName:'入库类型',template:'col_godowntype'},
            "col_holdday":{name:'col_18',displayName:'持票天数',template:'col_holdday'},
            "col_bestprice":{name:'col_19',displayName:'当日最优价(%)',template:'col_bestprice'},
            "col_lowestrate":{name:'col_20',displayName:'当日最低贴息',template:'col_lowestrate'},
            "col_bestreceive":{name:'col_21',displayName:'当日最优收款',template:'col_bestreceive'},
            "col_godownprice":{name:'col_22',displayName:'入库价格(%)',template:'col_godownprice'},
            "col_needpay":{name:'col_23',displayName:'应付金额(元)',template:'col_needpay'},
            "col_bestprof":{name:'col_24',displayName:'当日最优收益',template:'col_bestprof'},
            "col_storestatus":{name:'col_25',displayName:'状态',template:'col_storestatus'},
            "col_adjustday":{name:'col_26',displayName:'调整天数',template:'col_adjustday'},
            "col_godownremain":{name:'col_27',displayName:'转入剩余期限',template:'col_godownremain'},
            "col_outremain":{name:'col_28',displayName:'转出剩余期限',template:'col_outremain'},
            "col_needreceive":{name:'col_29',displayName:'应收金额(元)',template:'col_needreceive'},
            "col_margin":{name:'col_30',displayName:'点差(bp)',template:'col_margin'},
            "col_totalprof":{name:'col_31',displayName:'总收益',template:'col_totalprof'},
            "col_operall":{name:'col_32',displayName:'操作',template:'col_operall'},
        };


        this.templateDefine = new Map([
            ['col_outdate', '<div class="ui-grid-cell-contents">{{row.entity.outDate | date:"yyyy/MM/dd"}}</div>'],
            ['col_billtype', '<div class="ui-grid-cell-contents">{{ (row.entity.billInfoDto.billMedium&&row.entity.billInfoDto.billType) ? ((row.entity.billInfoDto.billMedium === "ELE"?"电":"纸") + (row.entity.billInfoDto.billType === "CMB"?"商":"银")) : "" }}</div>'],
            ['col_billnumber', '<div class="ui-grid-cell-contents">{{row.entity.billInfoDto.billNumber}}<div>'],
            ['col_amount', '<div class="ui-grid-cell-contents">{{row.entity.billInfoDto.amount | numberFilter}}<div>'],
            ['col_drawername', '<div class="ui-grid-cell-contents">{{row.entity.billInfoDto.drawerName}}</div>'],
            ['col_payeename', '<div class="ui-grid-cell-contents">{{row.entity.billInfoDto.payeeName}}<div>'],
            ['col_acceptingcompanyname', '<div class="ui-grid-cell-contents">{{row.entity.billInfoDto.acceptingCompanyName}}</div>'],
            ['col_acceptingcompanytype', '<div class="ui-grid-cell-contents">{{grid.appScope.dataDefineService.acceptingCompanyType[row.entity.billInfoDto.acceptingCompanyType]}}</div>'],
            ['col_billstartdate', '<div class="ui-grid-cell-contents">{{row.entity.billInfoDto.billStartDate | date:"yyyy/MM/dd"}}</div>'],
            ['col_billduedate', '<div class="ui-grid-cell-contents">{{row.entity.billInfoDto.billDueDate.formatDate("yyyy/MM/dd EEEE")}}</div>'],
            ['col_outcompany', '<div class="ui-grid-cell-contents">{{row.entity.counterPartyName}}</div>'],
            ['col_outprice', '<div class="ui-grid-cell-contents">{{row.entity.outPrice}}</div>'],
            ['col_outtype', '<div class="ui-grid-cell-contents">{{grid.appScope.dataDefineService.outType[row.entity.outType]}}</div>'],
            ['col_godowndate','<div class="ui-grid-cell-contents" >{{row.entity.godownDate | date:"yyyy/MM/dd"}}</div>'],
            ['col_remainday',  '<div class="ui-grid-cell-contents">{{row.entity.billInfoDto.theRemainingTerm}}</div>'],
            ['col_godowntype',  '<div class="ui-grid-cell-contents">{{grid.appScope.dataDefineService.godownType[row.entity.godownType]}}</div>'],
            ['col_holdday', '<div class="ui-grid-cell-contents">{{row.entity.ticketDays}}</div>'],
            ['col_bestprice', '<div class="ui-grid-cell-contents">{{row.entity.bestPrice}}</div>'],
            ['col_lowestrate', '<div class="ui-grid-cell-contents">{{row.entity.lowestDiscount}}</div>'],
            ['col_bestreceive', '<div class="ui-grid-cell-contents">{{row.entity.bestGathering}}</div>'],
            ['col_godownprice', '<div class="ui-grid-cell-contents">{{row.entity.godownPrice}}</div>'],
            ['col_needpay', '<div class="ui-grid-cell-contents">{{row.entity.amountsPayable | numberFilter}}</div>'],
            ['col_bestprof', '<div class="ui-grid-cell-contents">{{row.entity.bestIncome}}</div>'],
            ['col_storestatus', '<div class="ui-grid-cell-contents">{{grid.appScope.dataDefineService.billStatus[row.entity.storeStatus]}}</div>'],
            ['col_adjustday', '<div class="ui-grid-cell-contents">{{row.entity.adjustDays}}</div>'],
            ['col_godownremain', '<div class="ui-grid-cell-contents">{{row.entity.remainingTermIn}}</div>'],
            ['col_outremain', '<div class="ui-grid-cell-contents">{{row.entity.remainingTermOut}}</div>'],
            ['col_needreceive', '<div class="ui-grid-cell-contents">{{row.entity.amountDue | numberFilter}}</div>'],
            ['col_margin', '<div class="ui-grid-cell-contents">{{row.entity.pointDifference}}</div>'],
            ['col_totalprof', '<div class="ui-grid-cell-contents">{{row.entity.totalIncome}}</div>'],
            ['col_oper','<div class="ui-grid-cell-contents"><i class="ss-icon ss-icon-edit-normal ss-icon-highlight-bg pointer" tag="edit"><md-tooltip class="tooltip" md-direction="right">编辑</md-tooltip></i> <i class="ss-icon ss-icon-delete-normal ss-icon-highlight-bg pointer delete-bg" tag="del"><md-tooltip class="tooltip" md-direction="right">撤销</md-tooltip></i>'],
            ['col_operall','<div class="ui-grid-cell-contents"><i class="ss-icon ss-icon-delete-normal ss-icon-highlight-bg pointer delete-bg" tag="del"><md-tooltip class="tooltip" md-direction="right">撤销</md-tooltip></i></div>'],
        ]);

        this.inColum = [
            this.columsDef.col_oper,
            this.columsDef.col_godowndate,
            this.columsDef.col_billtype,
            this.columsDef.col_billnumber,
            this.columsDef.col_amount,
            this.columsDef.col_drawername,
            this.columsDef.col_payeename,
            this.columsDef.col_acceptingcompanyname,
            this.columsDef.col_acceptingcompanytype,
            this.columsDef.col_billstartdate,
            this.columsDef.col_billduedate,
            this.columsDef.col_godownremain,
            this.columsDef.col_godowntype,
            this.columsDef.col_godownprice,
            this.columsDef.col_needpay,
            this.columsDef.col_holdday,
            this.columsDef.col_bestprice,
            this.columsDef.col_lowestrate,
            this.columsDef.col_bestreceive,
            this.columsDef.col_bestprof,
        ];

        this.inColumWidth = {
            "col_oper" :{width:50},
            "col_godowndate" :{width:100},
            "col_billtype" :{width:100},
            "col_billnumber" :{width:100},
            "col_amount" :{width:100},
            "col_drawername" :{width:100},
            "col_payeename" :{width:100},
            "col_acceptingcompanyname" :{width:100},
            "col_acceptingcompanytype" :{width:100},
            "col_billstartdate" :{width:100},
            "col_billduedate" :{width:140},
            "col_remainday" :{width:100},
            "col_godowntype" :{width:100},
            "col_godownprice" :{width:100},
            "col_needpay" :{width:100},
            "col_holdday" :{width:100},
            "col_bestprice" :{width:140},
            "col_lowestrate" :{width:130},
            "col_bestreceive" :{width:130},
            "col_bestprof" :{width:150},
        };

        this.outColum = [
            this.columsDef.col_oper,
            this.columsDef.col_outdate,
            this.columsDef.col_billtype,
            this.columsDef.col_billnumber,
            this.columsDef.col_amount,
            this.columsDef.col_drawername,
            this.columsDef.col_payeename,
            this.columsDef.col_acceptingcompanyname,
            this.columsDef.col_acceptingcompanytype,
            this.columsDef.col_billstartdate,
            this.columsDef.col_billduedate,
            this.columsDef.col_outcompany,
            this.columsDef.col_outprice,
            this.columsDef.col_outtype,
        ];

        this.outColumWidth = {
            "col_outdate" :{width:100},
            "col_billtype" :{width:100},
            "col_billnumber" :{width:100},
            "col_amount" :{width:100},
            "col_drawername" :{width:100},
            "col_payeename" :{width:100},
            "col_acceptingcompanyname" :{width:100},
            "col_acceptingcompanytype" :{width:100},
            "col_billstartdate" :{width:100},
            "col_billduedate" :{width:140},
            "col_outcompany" :{width:100},
            "col_outprice" :{width:100},
            "col_outtype" :{width:100},
            "col_oper" :{width:100},
        };


        this.allColum = [
            this.columsDef.col_oper,
            this.columsDef.col_storestatus,
            this.columsDef.col_billtype,
            this.columsDef.col_billnumber,
            this.columsDef.col_amount,
            this.columsDef.col_drawername,
            this.columsDef.col_payeename,
            this.columsDef.col_acceptingcompanyname,
            this.columsDef.col_acceptingcompanytype,
            this.columsDef.col_billstartdate,
            this.columsDef.col_billduedate,
            this.columsDef.col_adjustday,
            this.columsDef.col_godowndate,
            this.columsDef.col_godownremain,
            this.columsDef.col_godownprice,
            this.columsDef.col_outdate,
            this.columsDef.col_outremain,
            this.columsDef.col_outprice,
            this.columsDef.col_outcompany,
            this.columsDef.col_needpay,
            this.columsDef.col_needreceive,
            this.columsDef.col_margin,
            this.columsDef.col_totalprof,
        ];

        this.allColumWidth = {
            "col_storestatus" :{width:70},
            "col_billtype" :{width:60},
            "col_billnumber" :{width:170},
            "col_amount" :{width:100},
            "col_drawername" :{width:100},
            "col_payeename" :{width:100},
            "col_acceptingcompanyname" :{width:100},
            "col_acceptingcompanytype" :{width:100},
            "col_billstartdate" :{width:100},
            "col_billduedate" :{width:140},
            "col_adjustday" :{width:80},
            "col_godowndate" :{width:100},
            "col_godownremain" :{width:140},
            "col_godownprice" :{width:100},
            "col_outdate" :{width:100},
            "col_outremain":{width:120},
            "col_outprice":{width:100},
            "col_outcompany" :{width:100},
            "col_needpay" :{width:100},
            "col_needreceive" :{width:100},
            "col_margin" :{width:100},
            "col_totalprof":{width:100},
            "col_operall" :{width:50}
        };

        this.spin = new Spinner({ color: '#fff', lines: 12, top: '30%' });
    }

    viewBusy(bBusy){
            if(bBusy){
                this.spin.spin(document.getElementById("gridViewBusy"));
            }else{
                this.spin.stop();
            }   
    }
    //重置grid大小，自动调整grid高度到底部
    resizeGrid (chart) {
        this.$interval(function () {
            var winHeight = window.innerHeight;
            if (!$('.ui-grid').offset()) return;
            var gridOffsetTop = $('.ui-grid').offset().top;
            if (chart) {
                let chartHeight = $(chart).height();
                $('.ui-grid').height(winHeight - gridOffsetTop - 5 - chartHeight);
            } else {
                $('.ui-grid').height(winHeight - gridOffsetTop - 5);
            }
        }, 10, 30);
    };

        //计算最后更新时间
    getLastUpdateTime (time) {
        if (!time) return;
        let now = new Date();
        now.setHours(0);
        now.setMinutes(0);
        now.setSeconds(0);
        now.setMilliseconds(0);

        return parseInt(time) > now.getTime();
    }

        //无限滚动加载数据
    getDataDown (scope, url, param) {
        this.storeManageService.storeSearchByParam(url,param)
            .then(res=>{
                if(res && res.result){
                    scope.gridOptions.data = scope.gridOptions.data.concat(res.result);
                }
            })        
    };

    gridInit(scope,url,param){
        this.viewBusy(true);
        scope.gridOptions.data = [];
        return this.storeManageService.storeSearchByParam(url,param)
            .then(res=>{
                if(res && res.result){
                    scope.gridOptions.data = res.result;
                }
                this.viewBusy(false);
            },res=>{
                this.viewBusy(false);
        });
    };

    columnFactory (define, templateDefine) {
        return {
            name: define.name,
            displayName: define.displayName,
            width: define.width || 100,
            minWidth: define.minWidth || 50,
            maxWidth: define.maxWidth,
            visible: true,
            cellTemplate: templateDefine.get(define.template)
        };
    };

    buildColumn(columnDefs,templateDefine){
        return columnDefs.map(e => this.columnFactory(e,templateDefine));
    };

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

            //是否允许调整列宽度
            enableColumnResizing: true,
            //是否允许调整列的顺序
            enableColumnMoving: true,
            //是否无限滚动，默认true
            enableInfiniteScroll: true,
            //滚动到距离底部10条时加载数据，默认20
            infiniteScrollRowsFromEnd: 20,
            //是否允许向下无限滚动，默认true
            infiniteScrollDown: true,
            //是否允许向上无限滚动，默认true
            infiniteScrollUp: false,

            //是否允许多选
            multiSelect: true,
            //是否允许键盘辅助多选
            modifierKeysToMultiSelect: true,
            //是否允许不选
            noUnselect: false,
            //是否显示选择列
            enableRowHeaderSelection: false,

            //是否保存这些数据，默认都是true
            saveWidths: true,
            saveVisible: true,
            saveOrder: true,
            saveScroll: false,
            saveFocus: false,
            saveSort: false,
            saveFilter: false,
            savePinning: true,
            saveGrouping: false,
            saveGroupingExpandedStates: false,
            saveTreeView: false,
            saveSelection: false,

            headerTemplate: this[getHeaderTemplate](),

            //ui-grid回调函数注册
            onRegisterApi: (gridApi) => {
                scope.gridApi = gridApi;
                gridApi.infiniteScroll.on.needLoadMoreData(scope,scope.getDataDown);

                gridApi.core.on.canvasHeightChanged(scope, (oldHeight, newHeight) => {

                    if (scope.type === scope.userInfoId + 'store' + 'all') {
                        let defaultState = scope.type + 'DefaultState';
                        let defaultItem = localStorage.getItem(defaultState);
                        if (!defaultItem) {
                            scope.saveState(defaultState);
                            localStorage.setItem(defaultState, JSON.stringify(scope[defaultState]));

                            let ITSState = scope.userInfoId + 'store' + 'its' + 'DefaultState';
                            scope[ITSState] = angular.copy(scope[defaultState]);
                            let ITSCols = angular.copy(this.getColDef('ITS'));
                            ITSCols.forEach(e => {
                                delete e.displayName;
                                delete e.template;
                                e.visible = true;
                            });
                            scope[ITSState].columns = ITSCols;
                            localStorage.setItem(ITSState, JSON.stringify(scope[ITSState]));

                            let OTSState = scope.userInfoId + 'store' + 'ots' + 'DefaultState';
                            scope[OTSState] = angular.copy(scope[defaultState]);
                            let OTSCols = angular.copy(this.getColDef('OTS'));
                            OTSCols.forEach(e => {
                                delete e.displayName;
                                delete e.template;
                                e.visible = true;
                            });
                            scope[OTSState].columns = OTSCols;
                            localStorage.setItem(OTSState, JSON.stringify(scope[OTSState]));
                        }

                        if (!scope.type || !scope.isStateInit) return;
                        let state = scope.type + 'State';
                        let item = localStorage.getItem(state);
                        if (item) {
                            scope[state] = JSON.parse(item);
                            scope.isStateInit = false;
                            scope.restoreState(state);
                        }
                    }
                });

                gridApi.colMovable.on.columnPositionChanged(scope, (colDef, originalPosition, newPosition) => {
                    scope.saveState(scope.type + 'State');
                    localStorage.setItem(scope.type + 'State', JSON.stringify(scope[scope.type + 'State']));
                });

                gridApi.colResizable.on.columnSizeChanged(scope, (colDef, deltaChange) => {
                    scope.saveState(scope.type + 'State');
                    localStorage.setItem(scope.type + 'State', JSON.stringify(scope[scope.type + 'State']));
                });
            },
            rowTemplate: '<div ng-repeat="col in colContainer.renderedColumns track by col.colDef.name" ng-class="{warning:row.entity.isNew}" class="ui-grid-cell" ui-grid-cell></div>',
            columnDefs: this.buildColumn(columnDefs, templateDefine)
        };
        return options;
    };

    getColDef(gridType){
        switch(gridType){
            case 'ALL':
            return this[converColWith](this.allColum,this.allColumWidth);
            case 'ITS':
            return this[converColWith](this.inColum,this.inColumWidth);
            case 'OTS':
            return this[converColWith](this.outColum,this.outColumWidth);
            default:
            return undefined;
        }
    };

    buildITS(panelParam,param){
        if(panelParam && panelParam.inType && angular.isArray(panelParam.inType) && panelParam.inType.length > 0){
            param.godownType = [];
            panelParam.inType.forEach(e =>{
                param.godownType.push(e.code);
            });
        }
    };

    buildOTS(panelParam,param){
        if(panelParam && panelParam.outType && angular.isArray(panelParam.outType) && panelParam.outType.length > 0){
            param.outType = [];
            panelParam.outType.forEach(e =>{
                param.outType.push(e.code);
            });
        }
    };

    buildQueryParam(paramArray){
        var param = {
            pageNumber:0,
            pageSize:50,
            paging:true,
        };

        if(paramArray.barParam.storeType.code && paramArray.barParam.storeType.code !== "ALL"){
            param.babStoreStatus = paramArray.barParam.storeType.code;
        }

        if(paramArray.panelParam && paramArray.panelParam.billType && angular.isArray(paramArray.panelParam.billType)){
            param.webBillType = [];
            paramArray.panelParam.billType.forEach(e=>{
                param.webBillType.push(e.code);
            });
        }

        if(paramArray.panelParam && paramArray.panelParam.dateType){
            param.billDueDateStart = paramArray.panelParam.dateType.start.getTime();
            param.billDueDateEnd = paramArray.panelParam.dateType.end.getTime();
        }

        switch(paramArray.barParam.storeType.code){
            case "ITS":
            this.buildITS(paramArray.panelParam,param);
            break;
            case "OTS":
            this.buildOTS(paramArray.panelParam,param);
            break;
            default:
            break;
        }

        return param;
    };

    getMenuData ($scope) {
        return [
            {
                title: '自定义列',
                icon: false,
                event: (event, obj) => {
                    this.openGridColumnPanel(event, {
                        columnDefs: $scope.columnDefs,
                        scope: $scope
                    });
                }
            },
            {
                title: '恢复默认',
                icon: false,
                event: (event, obj) => {
                    $scope.isStateInit = false;
                    $scope.quoteStatus = $scope.quoteStatus || '';
                    localStorage.removeItem($scope.type + 'State' + $scope.quoteStatus);
                    $scope[$scope.type + 'State' +  $scope.quoteStatus] = {};
                    let defaultState = $scope.type + 'DefaultState' + $scope.quoteStatus;
                    let defaultItem = localStorage.getItem(defaultState);
                    if (defaultItem) {
                        $scope[defaultState] = JSON.parse(defaultItem);
                        $scope.restoreState(defaultState);
                        $scope.columnDefs.forEach(e => e.visible = true);
                    } else {
                        // let columnDefs = this.columnDefs.findWhere(e => e.SSR);
                        // columnDefs.forEach(e => {
                        //     if (!e[$scope.billType]) {
                        //         e.visible = false;
                        //     } else {
                        //         e.visible = true;
                        //     }
                        // })
                        // let templateDefine = this.templateDefine;
                        // $scope.gridOptions = this.buildGridOptions($scope, columnDefs, templateDefine);
                    }
                }
            }
        ]
    };

    saveState (scope, state) {
        scope[state] = scope.gridApi.saveState.save();
    }

    restoreState (scope, state) {
        scope.gridApi.saveState.restore(scope, scope[state]);
    }

    openGridColumnPanel (event, params) {
        var position = this.$mdPanel.newPanelPosition()
            .absolute()
            .center();

        var config = {
            attachTo: angular.element(document.body),
            controller: gridColumnPanelCtrl,
            controllerAs: '$ctrl',
            bindToController: true,
            panelClass: 'column-panel',
            theme: params.theme || THEME_NAME,
            locals: {
                title: params.title || '自定义列',
                image: params.image || undefined,
                textContent: params.message || "",
                quotContent: params.quotContent || "",
                okText: params.okText || "确认",
                cancelText: params.cancelText || "取消",
                onOkCallback: params.okCallback || (() => true),
                onCancelCallback: params.cancelCallback || (() => true),
                scope: params.scope,
                columnDefs: params.columnDefs
            },
            template: this[getGridColumnPanelTemplate](),
            position: position,
            // animation: panelAnimation,
            hasBackdrop: true,
            trapFocus: true,
            fullscreen: false,
            disableParentScroll: false,
            clickOutsideToClose: false
        };

        return this.$mdPanel.open(config);
    }


    [converColWith](colArray,widthArray){
        if(!angular.isArray(colArray)) return colArray;
        colArray.forEach(e =>{
            if(e.template && widthArray[e.template])
            {
                e.width = widthArray[e.template].width;
            }
        })

        return colArray;
    };

    [getHeaderTemplate] () {
        return require('../common/component/template/grid-header.html');
    }

    [getGridColumnPanelTemplate] () {
        return require('../component/template/grid_column_panel.html');
    }

};

export default ['uiGridConstants','$interval','httpService','dataDefineService','storeManageService', '$mdPanel', gridStoreDefineService];