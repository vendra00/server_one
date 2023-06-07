package com.adasoft.server_one.model.entity;

import com.adasoft.commons.model.entity.AbstractEntity;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "file_change_events")
public class FileChangeEvent extends AbstractEntity {

}
