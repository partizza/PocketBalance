package ua.agwebs.web.rest;


public interface PermissionService {

    boolean checkPermission(long bookId, long userId);
}
