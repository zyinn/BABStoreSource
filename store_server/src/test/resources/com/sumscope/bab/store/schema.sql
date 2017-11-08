-- -----------------------------------------------------
-- Table `bab_store`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `bab_store` ;

CREATE TABLE IF NOT EXISTS `bab_store` (
  `id` VARCHAR(32) NOT NULL COMMENT '库存记录ID，业务无关逻辑主键',
  `bill_info_id` VARCHAR(32) NULL COMMENT '票据信息数据ID',
  `godown_date` DATETIME NULL COMMENT '入库日期，该日期可由用户选择，即是成交日期',
  `godown_type` VARCHAR(3) NULL COMMENT '入库类型\n\n买入票据：BYI （Buying In）\n应收票据：RCB （Receivable）\n签发票据：SGN （Sign）\n其他：OTH （Others）',
  `godown_price` DECIMAL(6,3) NULL COMMENT '成交价格，单位：百分比。',
  `out_date` DATETIME NULL COMMENT '出库日期，既卖出日期',
  `out_type` VARCHAR(3) NULL COMMENT '出库类型：\n票据贴现：DSC （Discount）\n卖出票据：OFK （Offtake）\n票据质押：PDG（Pledge）\n应付票据：PYB（Payable)\n其他：OTH (Others)',
  `out_price` DECIMAL(6,3) NULL COMMENT '出库价格，单位：百分比。',
  `counterparty_name` VARCHAR(200) NULL COMMENT '出库单：交易对手方',
  `memo` VARCHAR(200) NULL COMMENT '备注',
  `store_status` VARCHAR(3) NULL COMMENT '库存状态：\n已入库：ITS （In Store）\n已出库：OTS （Out Store）\n已作废：CAL （Canceled）',
  `operator_id` VARCHAR(32) NULL COMMENT '交易人ID，IAM系统对应ID',
  `create_date` DATETIME NULL COMMENT '创建日期',
  `latest_update_date` DATETIME NULL COMMENT '最后更新日期',
  `deal_id` VARCHAR(32) NULL COMMENT '如果库存信息是由报价系统的成交单生成的，此处存储对应的成交单ID',
  `adjust_days` INT(1) NULL,
  PRIMARY KEY (`id`));

-- -----------------------------------------------------
-- Table `bill_info`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `bill_info` ;

CREATE TABLE IF NOT EXISTS `bill_info` (
  `id` VARCHAR(32) NOT NULL COMMENT '票据信息ID，逻辑主键',
  `bill_number` VARCHAR(45) NULL COMMENT '票据号，由票据本身决定的唯一号码。',
  `operator_id` VARCHAR(32) NULL,
  `bill_medium` VARCHAR(3) NULL COMMENT '票据介质：\n纸票：PAP\n银票：ELE\n\n与报价系统一致',
  `bill_type` VARCHAR(3) NULL COMMENT '票据类型：\n银行票据：BKB\n商业票据：CMB\n\n与报价系统一致',
  `amount` DECIMAL(15,2) NULL COMMENT '金额',
  `payee_name` VARCHAR(200) NULL COMMENT '收款人名称',
  `drawer_name` VARCHAR(200) NULL COMMENT '出票人名称，一般是机构名称',
  `accepting_company_name` VARCHAR(200) NULL COMMENT '银行票据：承兑银行名称\n商业票据：承兑机构名称',
  `accepting_company_type` VARCHAR(3) NULL COMMENT '承兑机构类型\n\n银行票据：\n国股:GG\n城商：CS\n农商：NS\n农信：NX\n农合：NH\n村镇：CZ\n外资：WZ\n财务公司：CW\n\n商业票据：\n央企：CET\n国企：SOE\n地方性企业：LET\n其他：OTH\n\n取值与报价系统一致',
  `bill_start_date` DATETIME NULL COMMENT '票据出票日期',
  `bill_due_date` DATETIME NULL COMMENT '票据到期日期',
  `jpg_info` VARCHAR(200) NULL COMMENT '票据图片',
  `create_date` DATETIME NULL COMMENT '创建日期',
  `latest_update_date` DATETIME NULL COMMENT '最后更新日期',
  `bill_status` VARCHAR(3) NULL COMMENT '票据状态：有效：VLD（Valid）过期：EPD (Expired)异常：ANM (Abnormal)',
  PRIMARY KEY (`id`));