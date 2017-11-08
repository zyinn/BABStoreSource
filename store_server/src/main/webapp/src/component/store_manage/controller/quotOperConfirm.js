
const initView = Symbol('initView');

class quotOperConfirmCtrl {
    constructor($mdDialog) {
        this.$mdDialog = $mdDialog;
        // this.theme = "ssAvalonUi";
        this[initView]();
    };

    [initView]() {
        console.debug('quotOperConfirmCtrl initView');

    };

    $onClickOk(event) {
        if (this.onOkCallback) {
            var returnValue = this.onOkCallback();

            if (returnValue === false) return;

            if (returnValue && returnValue.then && typeof returnValue.then === 'function') {
                returnValue.then(res => {
                    if (res && res === true) this.$mdDialog.hide();
                    return;
                }, res => {
                    console.log(res);
                    return;
                })

                return;
            }
        }

        this.$mdDialog.hide();
    };

    $onClickCancel(event) {
         if (this.onCancelCallback) {
            var returnValue = this.onCancelCallback();

            if (returnValue === false) return;

            if (returnValue && returnValue.then && typeof returnValue.then === 'function') {
                returnValue.then(res => {
                    if (res && res === true) this.$mdDialog.cancel();
                    return;
                }, res => {
                    console.log(res);
                    return;
                })

                return;
            }
        }

        this.$mdDialog.cancel();
    };

};

export default ["$mdDialog", quotOperConfirmCtrl]