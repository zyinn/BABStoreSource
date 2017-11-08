const initView = Symbol('initView');

const panelTemplate = require('../component/store_manage/template/panel_search_criteria.html');

class panelBaseSearchCriteriaCtrl {

    constructor($scope, commonService) {
        this.$scope = $scope;

        this.commonService = commonService;

        this[initView]();
    };

    onVmChanged() {
        
        // this.ngModelCtrl.$viewValue = this.itemSource.findWhere(e => e.attribute && e.attribute.ngModel).map(e => e.attribute.ngModel);
        // this.ngModelCtrl.$commitViewValue();
        var viewModel = {};

        this.itemSource.forEach(item => {

            if (!item) return;

            if (item.attribute && item.vm) {
                if (item.attribute.ngModel instanceof Array) {
                    if (item.component === "searchcriteriaAcceptinghouseQuotepricetype") {
                        this.commonService.setPropertyX(viewModel, item.attribute.vmType, item.attribute.ngModel);
                    } else {
                        this.commonService.setPropertyX(viewModel, item.vm, item.attribute.ngModel);
                    }
                } else {
                    if (item.attribute.ngModel) this.commonService.setPropertyX(viewModel, item.vm, item.attribute.ngModel);
                }
            }
        });

        this.ngModelCtrl.$viewValue = viewModel;
        this.ngModelCtrl.$commitViewValue();

        if (this.$onSearchCriteriaVmChanged) this.$onSearchCriteriaVmChanged(this.ngModelCtrl.$modelValue);
    };

    [initView]() {
        console.debug('panelBaseSearchCriteriaCtrl initView');
    };

    $onChanges(event) {
        if (event.ngModel) {

            if (event.ngModel.currentValue) {
                if (!(this.itemSource instanceof Array)) return;

                this.itemSource.forEach(item => {
                    if(item.attribute && item.attribute.itemSource && item.attribute.ngModel){
                        if(item.attribute.itemSource.length === item.attribute.ngModel.length){
                            item.attribute.ngModel = undefined;
                        }
                    }
                    else{
                        var modelValue = this.commonService.getPropertyX(this.ngModel, item.vm);
                        item.attribute.ngModel = modelValue;
                    }
                });

            } else {
                if (this.itemSource) this.itemSource.forEach((item, index) => {
                    item.attribute.ngModel = undefined;
                });
            }
        }
    };
};

export { panelBaseSearchCriteriaCtrl, panelTemplate };