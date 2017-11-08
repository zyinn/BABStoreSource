const initView = Symbol('initView');
const loginFailed = Symbol('loginFailed');
const qbLoginPromise = Symbol('qbLoginPromise');
// const onUserExpired = Symbol('onUserExpired');

class loginCtrl {

    constructor($scope, $q, $mdDialog, $mdSidenav, md5, userService, configConsts) {
        this.$scope = $scope;
        this.$q = $q;
        this.$mdDialog = $mdDialog;
        this.$mdSidenav = $mdSidenav;

        this.md5 = md5;
        this.userService = userService;

        this.configConsts = configConsts; 

        this[initView]();
    };

    onClickLogin(event) {
        this.viewBusy = true;

        var loginInfo = {
            userName: this.$scope.userName,
            password: this.md5.createHash(this.$scope.password || '')
        };

        this.userService.login(loginInfo).then(res => {
            this.viewBusy = false;

            var userInfo = this.userService.getUserInfo();

            this.$router.navigate(this.targetPage ? this.targetPage : ['Home']);
        }, res => {
            this.viewBusy = false;
            console.error(res);

            this[loginFailed](event, res);
        });

        // this.$router.navigate(['Home/StoreManage']);
    };

    onClickUser(event) {
        var scope = angular.element(event.target).scope();
        var selectUser = undefined;

        if (scope) selectUser = scope.user;

        if (selectUser) {
            this.$scope.userName = selectUser.userName;
            this.$scope.password = selectUser.password;
        }

        this.$mdSidenav('login-right').close();
    };

    // private function
    [initView]() {
        console.debug('loginCtrl initView');
        // console.debug(`this.$scope.theme = ${this.$scope.theme}`);
        console.debug(`loginCtrl this.theme = ${this.theme}`);

        this.$scope.userList = this.configConsts.alternativeUser || [];

        var defaultUser = this.$scope.userList.findItem(e => e.default);

        if (defaultUser) {
            this.$scope.userName = defaultUser.userName;
            this.$scope.password = defaultUser.password;
        }
    };

    [loginFailed](event, response) {

        var message = response ? '用户名或密码错误' : '无法连接到服务器';

        // Appending dialog to document.body to cover sidenav in docs app
        // Modal dialogs should fully cover application
        // to prevent interaction outside of dialog
        this.$mdDialog.show(
            this.$mdDialog.alert()
                .parent(angular.element(document.querySelector('md-button[type=submit]')))
                .theme("ssAvalonUi")
                .clickOutsideToClose(true)
                .title('错误')
                .textContent(message)
                .ariaLabel('错误')
                .ok('关闭')
                .targetEvent(event)
        );
    };

    $onInit() {

        this[qbLoginPromise] = this.userService.getLoginInfoAsync().then(loginInfo => {
            this.userService.login(loginInfo).then(res => {
                console.log("Auto login by QB's account.");
                // this.$router.navigate([this.targetPage ? 'Home/' + this.targetPage : 'Home']);
                this.$router.navigate(this.targetPage ? this.targetPage : ['Home']);
                return this.$q.resolve();
            }, res => {
                //this.$mdDialog.show(
                //    this.$mdDialog.alert()
                //        .parent(angular.element(document.querySelector('md-button[type=submit]')))
                //        .theme("ssAvalonUi")
                //        .clickOutsideToClose(true)
                //        .title('错误')
                //        .textContent('登陆失败')
                //        .ariaLabel('错误')
                //        .ok('关闭')
                //        .targetEvent(event)
                //);
                console.warn("Auto login by QB's account failed.")
                return this.$q.reject();
            });
        }, loginInfo => {
            console.warn("Auto login by QB's account failed.");
            return this.$q.reject();
        });

    };

    $routerOnActivate(currentInstruction, previousInstruction) {
        if (currentInstruction && currentInstruction) {
            this.targetPage = ['Home'];
        }

        if (this[qbLoginPromise]) {
            this[qbLoginPromise].then(res => { }, res => {
                this.showLoginPanel = true;
            });
        } else {
            this.showLoginPanel = true;
        }
    };
};

let login = () => {
    return {
        template: require('./template/login.html'),
        bindings: {
            theme: '@',
            $router: '<'
        },
        controller: ['$scope', '$q', '$mdDialog', '$mdSidenav', 'md5', 'userService', 'configConsts', loginCtrl]
    }
};

export default login;