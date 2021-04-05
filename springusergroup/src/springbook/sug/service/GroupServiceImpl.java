package springbook.sug.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import springbook.sug.dao.GroupDao;
import springbook.sug.domain.Group;

@Service
@Transactional
public class GroupServiceImpl implements GroupService {
	private GroupDao groupDao;

	@Autowired
	public void setGroupDao(GroupDao groupDao) {
		this.groupDao = groupDao;
	}

	public Group add(Group group) {
		return groupDao.add(group);
	}

	public void delete(int id) {
		groupDao.delete(id);
	}

	public Group get(int id) {
		return groupDao.get(id);
	}

	public Group update(Group group) {
		return groupDao.update(group);
	}

	public List<Group> getAll() {
		return groupDao.getAll();
	}
}
