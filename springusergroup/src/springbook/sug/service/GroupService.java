package springbook.sug.service;

import java.util.List;

import springbook.sug.domain.Group;

public interface GroupService extends GenericService<Group> {
	List<Group> getAll();
}
