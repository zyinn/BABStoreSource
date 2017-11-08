const initView = Symbol('initView');
const btnShowChange = Symbol(btnShowChange);
const THEME_NAME = "ssAvalonUi";
const dataDefine = {

    showTab: {
        "ALL": ["storeType", "inStore", "batDel", "import", "export"],
        "ITS": ["storeType", "inStore", "outStore", "quote", "batDel", "import", "export"],
        "OTS": ["storeType", "inStore", "batDel", "export"]
    },

    showPanel: {
        "ALL": ["billType", "dateType"],
        "ITS": ["billType", "inType", "dateType"],
        "OTS": ["billType", "outType", "dateType"]
    },

    filterBarDefine: [
        {
            id: "storeBar1", vm: "inStore", component: "mdButton", displayName: "入库", containerClass: "inline-block", containerAlien: "alien-left", bVisiable: true,
        },
        {
            id: "storeBar2", vm: "outStore", component: "mdButton", displayName: "出库", containerClass: "inline-block", containerAlien: "alien-left", bVisiable: false,
        },
        {
            id: "storeBar3", vm: "quote", component: "mdButton", displayName: "发布贴现", containerClass: "inline-block", containerAlien: "alien-left", bVisiable: false,
        },
        {
            id: "storeBar4", vm: "batDel", component: "mdButton", displayName: "批量删除", containerClass: "inline-block", containerAlien: "alien-left", bVisiable: true,
        },
        {
            id: "storeBar5", vm: "import", component: "mdButton", displayName: "导入", containerClass: "inline-block", containerAlien: "alien-left", bVisiable: true,
        },
        {
            id: "storeBar6", vm: "export", component: "mdButton", displayName: "导出Excel", containerClass: "inline-block", containerAlien: "alien-left", bVisiable: true,
        },
    ],
}

class gridStoreManageCtrl {

    constructor($scope,gridStoreDefineService,httpService,configConsts,$window,dataDefineService,storeManageService,componentCommonService, userService) {
        this.$scope = $scope;
        this.gridStoreDefineService = gridStoreDefineService;
        this.$scope.gridStoreDefineService = gridStoreDefineService;
        this.$scope.getLastUpdateTime = gridStoreDefineService.getLastUpdateTime;
        this.httpService = httpService;
        this.configConsts = configConsts
        this.dataDefineService = dataDefineService;
        this.$scope.dataDefineService = dataDefineService;
        this.storeManageService = storeManageService;
        this.componentCommonService = componentCommonService;
        this.userService = userService;

        window.onresize = function(){
            gridStoreDefineService.resizeGrid();
        }

        $scope.getDataDown = ()=>{
            this.quoteQueryParam.pageNumber++;
            gridStoreDefineService.getDataDown(this.$scope,this.configConsts.search_stores_by_param,this.quoteQueryParam);
        }

        $scope.saveState = (state) => {
            this.gridStoreDefineService.saveState($scope, state);
        };
        $scope.restoreState = (state) => {
            this.gridStoreDefineService.restoreState($scope, state);
        }
        $scope.data = this.gridStoreDefineService.getMenuData($scope);
        $scope.menuForced = {x: true, y: true};

        this.$scope.gridOptions = this.gridStoreDefineService.buildGridOptions($scope,this.gridStoreDefineService.getColDef('ALL'),this.gridStoreDefineService.templateDefine);
        this.$scope.gridOptions.data = [];
        $scope.columnDefs = this.$scope.gridOptions.columnDefs;
        $scope.columnDefs.forEach(e => e.visible = true);

        this[initView]();
    };

    [initView]() {
        this.gridStoreDefineService.resizeGrid();
        console.debug('gridStoreManageCtrl initView');
        this.filterBarItem = dataDefine.filterBarDefine;

        this.$scope.$on('deleteStore', () => {
            let sel = this.$scope.gridApi.selection.getSelectedRows();
            if (!sel || !angular.isArray(sel) || sel.length === 0) return;
            let lst = sel.map(e=>{return e.id});
            this.storeManageService.storeCancel(lst).then(res => {
                if (res.return_code === 0) {
                    lst.forEach(ee => {
                        this.$scope.gridOptions.data = this.$scope.gridOptions.data.findWhere(e => {
                            return e.id !== ee;
                        });
                    });
                }
            }, err => {
                let message = '批量删除出错！';
                if (err.return_code === -1 && err.return_message && err.return_message.errorDetailsList && angular.isArray(err.return_message.errorDetailsList) && err.return_message.errorDetailsList.length !== 0) {
                    message = err.return_message.errorDetailsList[0].detailMsg;
                }
                this.componentCommonService.openErrorDialog({
                    title: '错误提示',
                    theme: THEME_NAME,
                    message: message
                });
            });
        });

        this.$scope.$on('gridRefresh', (event, param) => {
            this.gridStoreDefineService.gridInit(this.$scope,this.configConsts.search_stores_by_param,this.quoteQueryParam);
        });
    };

    $onInit () {
        let userInfo = this.userService.getUserInfo();
        if (userInfo && userInfo.user && userInfo.user.id) {
            this.$scope.userInfoId = userInfo.user.id;
            this.$scope.type = this.$scope.userInfoId + 'store' + 'all';
        }
        this.$scope.isStateInit = true;
    }

    $onChanges (event){
        if(!event || !event.filterParam ) return;
        var pre = event.filterParam.previousValue;
        var cur = event.filterParam.currentValue;
        if(!cur || cur.length === 0 || angular.equals(pre,cur)) return;
        this[btnShowChange](cur.barParam.storeType.code);
        if(cur.barParam && cur.barParam.storeType){
            if(!pre.barParam|| !pre.barParam.storeType || cur.barParam.storeType.code !== pre.barParam.storeType.code){
                this.$scope.gridOptions = this.gridStoreDefineService.buildGridOptions(this.$scope,this.gridStoreDefineService.getColDef(cur.barParam.storeType.code),this.gridStoreDefineService.templateDefine);
                this.$scope.type = this.$scope.userInfoId + 'store' + cur.barParam.storeType.code.toLowerCase();
                this.$scope.columnDefs = this.$scope.gridOptions.columnDefs;
                this.$scope.columnDefs.forEach(e => e.visible = true);
            }
        }
        
        this.quoteQueryParam = this.gridStoreDefineService.buildQueryParam(cur);
        this.gridStoreDefineService.gridInit(this.$scope,this.configConsts.search_stores_by_param,this.quoteQueryParam).then(e => {
            let state = this.$scope.type + 'State';
            let item = localStorage.getItem(state);
            if (item) {
                this.$scope[state] = JSON.parse(item);
                this.$scope.isStateInit = false;
                this.$scope.restoreState(state);
            } else {
                let state = this.$scope.type + 'DefaultState';
                let item = localStorage.getItem(state);
                if (item) {
                    this.$scope[state] = JSON.parse(item);
                    this.$scope.isStateInit = false;
                    this.$scope.restoreState(state);
                }
            }
        });
    };

    onClickTable (event) {
        if (event && event.target) {
            var target = angular.element(event.target).scope();
            while (target && !target.hasOwnProperty('row')) target = target.$parent;

            if(target && target.row && target.row.entity){
                var att = event.target.getAttribute('tag');
                switch(att){
                    case 'edit':
                        if(this.editListItem){
                            var ret = this.editListItem({
                                $event:event,
                                item:target.row.entity
                            }).then(res => {  
                                this.gridStoreDefineService.gridInit(this.$scope,this.configConsts.search_stores_by_param,this.quoteQueryParam);
                            });
                        }
                        break;
                    case 'del':
                        if(this.delListItem){
                            var ret = this.delListItem({
                                $event:event,
                                item:target.row.entity
                            }).then(res =>{
                                this.gridStoreDefineService.gridInit(this.$scope,this.configConsts.search_stores_by_param,this.quoteQueryParam);
                            });
                        }
                        break;
                    default:
                    break;
                }
            }
        }
    };

    onBtnClick($event, dataDefine) {
        if(this.btnClick){
            this.btnClick({
                $event:$event,
                dataDefine:dataDefine
            });
        }
    }

    [btnShowChange](tabCode) {
        if (this.currentNavItem === tabCode) return;
        this.currentNavItem = tabCode;

        var showArray = dataDefine.showTab[tabCode];
        if (this.filterBarItem instanceof Array) {
            this.filterBarItem.forEach(e => {
                e.bVisiable = dataDefine.showTab[tabCode].findItem(e1 => e1 === e.vm) !== undefined;
            });
        }
    };

};

let gridStoreManage = () => {
    return {
        template: require('./template/grid_store_manage.html'),
        bindings: {
            theme: '@mdTheme',
            $router: '<',
            filterParam:'<',
            editListItem:'&',
            delListItem:'&',
            btnClick:'&'
        },
        controller: ["$scope","gridStoreDefineService","httpService","configConsts","$window","dataDefineService","storeManageService","componentCommonService", 'userService', gridStoreManageCtrl]
    };
};

export default gridStoreManage;