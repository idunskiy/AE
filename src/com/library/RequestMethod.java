package com.library;
/**  *	 ������������ ������� ��� REST �����������*/
public enum RequestMethod {
	/**  *GET �����*/
	GET,
	/**  *POST �����*/
    POST,
    /**  *PUT �����*/
    PUT,
    /**  *DELETE �����**/
    DELETE,
    /**  *POST ����� ��� �������� ������ (������������ MultipartEntity ��� setEntity ��� �������� ������)*/
    POST_UPLOAD
}
