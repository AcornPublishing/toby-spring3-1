package springbook.sug.web.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;

import springbook.sug.domain.Group;
import springbook.sug.service.GroupService;

public class GroupConverter {
	private GroupConverter() {}
	
	public static class GroupToString implements Converter<Group, String> {
		public String convert(Group group) {
			return (group == null) ? "" : String.valueOf(group.getId());
		}
	}
	
	public static class StringToGroup implements Converter<String, Group> {
		private GroupService groupService;
		
		@Autowired 
		public void setGroupService(GroupService groupService) {
			this.groupService = groupService;
		}

		public Group convert(String text) {
			return groupService.get(Integer.valueOf(text));
		}
	}
}
