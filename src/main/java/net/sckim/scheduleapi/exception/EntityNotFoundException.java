package net.sckim.scheduleapi.exception;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(Long id, Class<?> clazz) {
        super("존재하지 않는 데이터 : " + clazz.getSimpleName() + " ID " + id);
    }
}
