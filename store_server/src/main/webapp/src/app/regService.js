import mainModule from '../bab.store.main.module';

import gridStoreDefineService from '../service/gridStoreDefineService';
import dataDefineService from '../service/dataDefineService';
import gridExcelImportService from '../service/gridExcelImportService';
import storeManageService from '../service/storeManageService';

mainModule.service('gridStoreDefineService', gridStoreDefineService)
    .service('dataDefineService',dataDefineService)
    .service('gridExcelImportService', gridExcelImportService)
    .service('storeManageService',storeManageService);

export default mainModule;