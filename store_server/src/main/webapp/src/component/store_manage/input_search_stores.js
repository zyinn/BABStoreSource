const initView = Symbol('initView');
const THEME_NAME = "ssAvalonUi";
class inputSearchStoresCtrl {

    constructor($scope, $q, $timeout, storeManageService, componentCommonService, commonService) {
        this.$scope = $scope;
        this.$q = $q;
        this.$timeout = $timeout;

        this.storeManageService = storeManageService;
        this.componentCommonService = componentCommonService;
        this.commonService = commonService;

        this[initView]();
    };

    [initView]() {
        console.debug('inputAcceptinghouseSearchDropCtrl initView');

        // this.displayPath = "companyName";

        this.searchSource = "quick";
    };

    onVmChanged() {
        // this.selectedItem.$queryString = this.inputString;

        // if (this.selectedItem.type === undefined) {
        //     this.ngModelCtrl.$viewValue = this.selectedItem;
        //     this.ngModelCtrl.$commitViewValue();
        //     if (this.vmChanged) this.vmChanged();

        // } else {
        //     if (this.selectedItem.type === 'searchMore') {
        //         debugger;
        //         this.searchMoreText = this.selectedItem.queryString;
        //     }

        // }

        this.ngModel = {
            selectedItem: this.selectedItem,
            inputString: this.searchText
        };

        this.ngModelCtrl.$viewValue = this.ngModel;
        this.ngModelCtrl.$commitViewValue();
        if (this.vmChanged) this.vmChanged();
    };

    searchMore(event) {

        if (!this.searchText || this.searchText === "") return this.searchResult = this.$q.reject();

        if (this.searchResult && this.searchResult.$$state && this.searchResult.$$state.status === 0) return this.searchResult = this.$q.reject();

        if (this.searchTaskTimer) this.$timeout.cancel(this.searchTaskTimer);

        if (this.searchText.length < 16 || this.searchText.length > 16 && this.searchText.length < 30) return this.searchResult = this.$q.reject();

        var defer = this.$q.defer();

        // this.doSearch(defer, true);
        this.storeManageService.babStoreManageSrarchStores(this.searchText, false).then(res => {
            if (!res || !res.result) {
                defer.reject();
            }

            defer.resolve(res.result);

            this.$timeout(() => {
                if (event && event.target) {

                    var element = $(event.target);

                    element = element.parentsUntil('input-acceptinghouse-search-drop').find('input');

                    if (element && element.length > 0) {
                        element.focus();
                    }
                }
            }, 500, true);

        }, res => {
            defer.reject();
        });

        this.searchResult = defer.promise;
    };

    onSelectedItemChange() {

        // console.debug('onSelectedItemChange');

        this.ngModel = {
            selectedItem: this.selectedItem,
            inputString: this.searchText
        };

        this.ngModelCtrl.$viewValue = this.ngModel;
        this.ngModelCtrl.$commitViewValue();
        if (this.vmChanged) this.vmChanged();
    };

    $onChanges(event) {
        if (!event) return;

        if (event.ngModel) {
            if (event.ngModel.currentValue) {
                this.selectedItem = this.ngModel.selectedItem;
                this.searchText = this.ngModel.inputString;
            } else {
                this.selectedItem = undefined;
                this.searchText = undefined;
            }
        }
    };

    //B6C9E250704C69CV
    //16C9E240704C83FF16C9E240704C83 
    //B6C9E250704C8899
    //16C9E240704C83FF16C9E240704C83
    //B6C9E260704C5210
    //B6C9E260704C1143
    //B6C9E260704C5210
    //16C9E240704C69CV16C9E240704C69
    //B6C9E250704C3344
    //B6C9E250704C69CV
    //16C9E240704C521016C9E240704C52
    //B6C9E250704C83FF  
    //B6C9E260704C7766
    //16C9E240704C69CV16C9E240704C69
    //B6C9E260704C1143
    //电银：30位，第1位为1
    //纸银：16位，第7位为5
    //纸商：16位，第7位为6
    //电商：30位，第1位为2
    searchTextChange() {

        if (!this.ngModel) this.ngModel = {};   
        this.ngModel.inputString = this.searchText;
        this.ngModel.selectedItem = undefined;
        this.ngModelCtrl.$viewValue = this.ngModel;
        this.ngModelCtrl.$commitViewValue();
        if (this.vmChanged) this.vmChanged();

        if (!this.searchText || this.searchText === "") return this.searchResult = this.$q.reject();

        if (this.searchText.length < 16 || this.searchText.length > 16 && this.searchText.length < 30) return this.searchResult = this.$q.reject();  

        if (this.searchTaskTimer) this.$timeout.cancel(this.searchTaskTimer);

        var defer = this.$q.defer();

        this.searchTaskTimer = this.$timeout(() => {
            if (!this.searchText || this.searchText === "") {
                this.$timeout.cancel(this.searchTaskTimer);
                return;
            }

            console.debug(`Begin search: ${this.searchText}`);

            this.storeManageService.babStoreManageSrarchStores(this.searchText, false).then(res => { 

                if (!res || !res.result || !res.result.length || !res.result[0].billInfoDto) {
                    defer.reject();
                }

                defer.resolve([]);
                let result = res.result[0].billInfoUsage;
                let message = '';
                if (result === 'USE_BY_CURRENT') {
                    message = '该票据库存中已存在！'
                } else if (result === 'USE_BY_OTHERS') {
                    message = '该票据市场上已存在，请确认票号是否正确。'
                }
                if (message) {
                    this.componentCommonService.openErrorDialog({
                        title: '提示',
                        theme: THEME_NAME,
                        message: message
                    });
                }
            }, res => {
                defer.reject();
            });
        }, 500, true);

        this.searchResult = defer.promise;
    };
};

let inputSearchStores = () => {

    return {
        template: require('./template/input_search_stores.html'),
        require: {
            ngModelCtrl: 'ngModel',
        },
        bindings: {
            theme: '@',
            label: '@',
            labelFlex: '<',
            inputFlex: '<',
            ngModel: '<',
            isRequired: '@',
            placeholder: '@',  
            
            isDisabled: '@',

            vmChanged: '&'
        },
        controller: ['$scope', '$q', '$timeout', 'storeManageService', 'componentCommonService', 'commonService', inputSearchStoresCtrl]
    }
};

export default inputSearchStores;