package com.blogsculpture.event;

import org.springframework.context.ApplicationEvent;
import com.blogsculpture.model.User;
import lombok.Data;

@Data
public class RegistrationCompleteEvent extends ApplicationEvent {

	private User user;
	private String conformationUrl;

	public RegistrationCompleteEvent(User user, String conformationUrl) {
		super(user);
		this.user = user;
		this.conformationUrl = conformationUrl;
	}
}
