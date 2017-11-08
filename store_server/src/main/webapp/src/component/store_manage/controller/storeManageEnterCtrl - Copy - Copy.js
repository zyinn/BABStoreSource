import panelBaseDialogCtrl from './../../../common/controller/panelBaseDialogCtrl';

const initCtrl = Symbol('initCtrl');

class storeManageEnterCtrl extends panelBaseDialogCtrl {
    constructor($sce, commonService, configConsts) {
        super($sce, commonService, configConsts);

        this[initView]();
    };

    [initCtrl]() {
        console.debug('storeManageEnterCtrl initCtrl');    
    };                 

    $onDialogVmChanging(result) {
        if (!result) return;  
    };

    $onDialogVmChanged(result) {
        if (!result) return;                 
    };
};

export default ['$sce', 'commonService', 'configConsts', addPriceSscCtrl]