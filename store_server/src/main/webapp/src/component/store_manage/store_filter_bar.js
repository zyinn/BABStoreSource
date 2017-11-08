const initView = Symbol('initView');

class storeFilterBarCtrl {

    constructor(commonService) {
        this.commonService = commonService;
        this[initView]();
    };


    [initView]() {
        console.debug('storeFilterBarCtrl initView');
    };

    onTabChanged(tabCode) {
        if (this.tabChanged) this.tabChanged({ tabCode: tabCode });
    };

    onClickButton(event) {
        if (!event || !event.target) return;
        var button = angular.element(event.target);
        if (event.target.nodeName !== 'BUTTON') button = button.parent();
        if (!button) return;
        if (button.attr('aria-label') === 'mdButton') {
            var item = button.scope().item;
            this.btnClick({
                $event: event,
                dataDefine: item
            });
        }
    };

    onVmChanged(){
        var param = {};
        this.itemSource.forEach(e=>{
            if(!e.vm) return;
            if(e.attribute && e.attribute.ngModel){
                this.commonService.setPropertyX(param,e.vm,e.attribute.ngModel);
            }
            this.ngModelCtrl.$viewValue = param;
            this.ngModelCtrl.$commitViewValue();
        })
    };
};

let storeFilterBar = () => {
    return {
        template: require('./template/store_filter_bar.html'),
        require: {
            ngModelCtrl: 'ngModel',
        },
        bindings: {
            theme: '@mdTheme',
            itemSource:'<',

            btnClick: '&',
            tabChanged: '&',
            ngModel:'<'
        },
        controller: ['commonService',storeFilterBarCtrl]
    };
};

export default storeFilterBar;