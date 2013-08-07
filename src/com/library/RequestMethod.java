package com.library;
/**  *	 перечисление методов для REST архитектуры*/
public enum RequestMethod {
	/**  *GET метод*/
	GET,
	/**  *POST метод*/
    POST,
    /**  *PUT метод*/
    PUT,
    /**  *DELETE метод**/
    DELETE,
    /**  *POST метод для загрузки файлов (используется MultipartEntity для setEntity для передачи файлов)*/
    POST_UPLOAD
}
