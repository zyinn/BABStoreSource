const initView = Symbol('initView');
const $closeDialog = Symbol('$closeDialog');
const $dialogResult = Symbol('$dialogResult');

class panelBaseDialogCtrl {

    constructor($sce, commonService, configConsts) {
        this.commonService = commonService;
        this.configConsts = configConsts;
        this.$sce = $sce;

        this[initView]();
    };

    [initView]() {
        console.debug('panelBaseDialogCtrl initView');

        this[$dialogResult] = undefined;

        this.labelFlex = 20;
        this.inputFlex = 80;

        if (this.itemSource && this.itemSource instanceof Array) {
            var item = this.itemSource.findItem(e => e.component === 'labelHtmlContent');

            if (item) item.html = this.$sce.trustAsHtml(item.html);
        }

    };

    onVmChanged(prop) {
        if (!this.itemSource || !(this.itemSource instanceof Array)) return;

        if (this.$onDialogVmChanging) this.$onDialogVmChanging(this[$dialogResult], prop);

        this[$dialogResult] = {}

        this.itemSource.forEach(e => {

            if (!e.vm) return;

            if (e.attribute && e.attribute.ngModel && e.attribute.ngModel.vmValue !== undefined) {
                this.commonService.setPropertyX(this[$dialogResult], e.vm, e.attribute.ngModel.vmValue);
            } else {
                this.commonService.setPropertyX(this[$dialogResult], e.vm, e.attribute.ngModel);
            }
        });

        if (this.$onDialogVmChanged) this.$onDialogVmChanged(this[$dialogResult], prop);
    };

    $onClickClear(event) {
        if (!this.itemSource || !(this.itemSource instanceof Array)) return;

        this[$dialogResult] = {}

        this.itemSource.forEach(e => {
            this.commonService.setPropertyX(e, "attribute.ngModel", undefined);
        });
    };

    $onClickOk(event) {
        if (this.onClosing) {
            var returnValue = this.onClosing(this[$dialogResult]);

            if (returnValue === false) return;

            if (returnValue && returnValue.then && typeof returnValue.then === 'function') {
                returnValue.then(res => {
                    if (res && res === true) this.mdPanelRef.close();
                    return;
                }, res => {
                    if(res) console.error(res);
                    return;
                })

                return;
            }
        }

        this.mdPanelRef.close();
    };

    $onClickCancel(event) {
        this.mdPanelRef && this.mdPanelRef.close().then(() => {
            this.mdPanelRef.destroy();
        });
    };

    $setDialogResult(result) {
        this[$dialogResult] = result;
    };
};

export default panelBaseDialogCtrl;