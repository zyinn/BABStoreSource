const init = Symbol('init');
const THEME_NAME = "ssAvalonUi";

class storeManageService {
    constructor(httpService, configConsts, userService, $q) {

        this.httpService = httpService;
        this.configConsts = configConsts;
        this.userService = userService;
        this.$q = $q;
        this[init]();
    };

    [init]() {
        console.debug('storeManageService initialized.');
    };

    getAuthHeaders() {
        return this.userService.getAuthHeaders();
    };

    storeInit() {
        // 使用QB账户的自动登录                
        return this.userService.getAuthHeadersAsync().then(headers => {
            if (!headers || !headers.token) return this.$q.resolve({ result: undefined });
            return this.httpService.postService(this.configConsts.bab_store_init, undefined, headers);
        });
    };

    createOrUpdateGoDownStores(dto, isUpdate) {

        let userInfo = this.userService.getUserInfo();

        if (!userInfo || !userInfo.user) {
            console.warn('storeManageService.createOrUpdateGoDownStores: Load userInfo failed.');
            return this.$q.reject();
        }
        if (angular.isArray(dto) && dto.length !== 0) {
            dto.forEach(item => {
                item.billInfoDtoList.forEach(e => {
                    if (e) {
                        e.billStatus = "VLD";
                        e.operatorId = userInfo.user.id;
                    }
                });
            })
        } else {
            if (dto.billInfoDtoList instanceof Array) {
                dto.billInfoDtoList.forEach(e => {
                    if (e) {
                        e.billStatus = "VLD";
                        e.operatorId = userInfo.user.id;
                    }
                });
                dto = isUpdate ? dto : [dto];
            }
        }

        // dto.godownType = "BYI";

        let headers = this.getAuthHeaders();
        if (!dto.quoteToken) console.warn('storeManageService.createOrUpdateGoDownStores: dto.quoteToken is null or empty.');
        headers.quoteToken = dto.quoteToken;

        let url = isUpdate ? this.configConsts.update_godown_store : this.configConsts.create_go_down_stores;

        return this.httpService.postService(url, dto, headers);
    };

    createOutStore(dto) {
        let headers = this.getAuthHeaders();
        if (!dto.quoteToken) console.warn('storeManageService.createOrUpdateGoDownStores: dto.quoteToken is null or empty.');
        headers.quoteToken = dto.quoteToken;

        return this.httpService.postService(this.configConsts.create_out_store, dto, headers);
    };

    createDiscount(dto, quoteToken) {
        let headers = this.getAuthHeaders();
        if (!quoteToken) console.warn('storeManageService.createDiscount: quoteToken is null or empty.');
        headers.quoteToken = quoteToken;

        return this.httpService.postService(this.configConsts.post_discount, dto, headers);
    };

    excelImportParser(param) {
        let headers = this.getAuthHeaders();
        return this.httpService.postService(this.configConsts.parser_go_down_stores, param, headers);
    };

    storeSetStatus(id) {
        let headers = this.getAuthHeaders();
        return this.httpService.postService(this.configConsts.update_godown_store, headers);
    };

    storeCancel(lst) {
        let headers = this.getAuthHeaders();
        return this.httpService.postService(this.configConsts.cancel_store, lst, headers);
    };

    babStoreManageSrarchStores(dto) {
        var url = this.configConsts.search_stores_by_code;
        let headers = this.getAuthHeaders();
        return this.httpService.postService(url, dto, headers);
    };

    storeSearchByParam(url, param) {
        let headers = this.getAuthHeaders();
        return this.httpService.postService(url, param, headers);
    };
}

export default ['httpService', 'configConsts', 'userService', '$q', storeManageService];