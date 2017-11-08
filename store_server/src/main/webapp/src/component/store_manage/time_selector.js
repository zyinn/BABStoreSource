const initView = Symbol('initView');

class timeSelectorCtrl {

    constructor(commonService) {
        this.commonService = commonService;
        this[initView]();
    };


    [initView]() {
        console.debug('timeSelectorCtrl initView');

        this.selectorItems = [{
            $id:0,
            code:"custom",
            name:"自定义"
        }];

        this.showDatePicker = false;

    };

    onSelectorChange(){
        if(this.selectorModel === undefined){
            this.ngModelCtrl.$viewValue = undefined;
            this.showDatePicker = false;
        }
        else if(this.selectorModel.code === 'custom'){
            this.ngModelCtrl.$viewValue = this.dateModel;
            this.showDatePicker = true;
        }
        
        this.ngModelCtrl.$commitViewValue();
        if(this.vmChanged) this.vmChanged();
    }

    onDateChanged(){
        if(this.selectorModel && this.selectorModel.code === 'custom'){
            this.ngModelCtrl.$viewValue = this.dateModel;
            this.ngModelCtrl.$commitViewValue();
            if(this.vmChanged) this.vmChanged();
        }
    }

    $onChanges(evt){
        if(evt.ngModel && !evt.ngModel.currentValue){
            this.selectorModel = undefined;
        }
    }

};

let timeSelector = () => {
    return {
        template: require('./template/time_selector.html'),
        require: {
            ngModelCtrl: 'ngModel',
        },
        bindings: {
            theme: '@mdTheme',
            ngModel:'<',
            label:'<',
            selectorFlex:'@',
            dataFlex:'@',
            vmChanged:'&'
        },
        controller: ['commonService',timeSelectorCtrl]
    };
};

export default timeSelector;