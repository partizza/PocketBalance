package ua.agwebs.web.rest;


public interface PermissionService {

    boolean checkPermission(long booId, long userId);
}
