package io.cms.core.module.system.enums;

public enum Status {

	/** 0 */ Init(0, "DICT_SYSTEM_STATUS_INIT"),
	/** 1 */ Valid(1, "DICT_SYSTEM_STATUS_VALID"),
	/** 2 */ Invalid(2, "DICT_SYSTEM_STATUS_INVALID"),
	/** 3 */ Deleted(3, "DICT_SYSTEM_STATUS_DELETED"),
	/** -1 */ Expired(-1, "DICT_SYSTEM_STATUS_EXPIRED");

	private int code;
	private String dictCode;

	private Status(int code, String dictCode) {
		this.code = code;
		this.dictCode = dictCode;
	}

	public int getCode() {
		return code;
	}

	public String getDictCode() {
		return dictCode;
	}

	public static Status valueOf(int value) {
		switch (value) {
		case 0:
			return Init;
		case 1:
			return Valid;
		case 2:
			return Invalid;
		case -1:
			return Expired;
		default:
			return Init;
		}
	}
}
