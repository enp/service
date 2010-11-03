/*
 * Copyright (c) 2010 Eugene Prokopiev <enp@itx.ru>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.itx.beans;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

@Entity
@Table(name="books")
public class Book implements Serializable {
	
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private int id;
	
	@Column(name="name",length=50)
	private String name;
	
	@Column(name="author",length=50)
	private String author;
	
	@Temporal(TemporalType.DATE)
	@Column(name="date")
	private Date date;
	
	public Book() {
		this.date = new Date();
	}
	
	public Book(String name, String author) {
		this.name = name;
		this.author = author;
		this.date = new Date();
	}
	
	public Book(String name, String author, Date date) {
		this.name = name;
		this.author = author;
		this.date = date;
	}
	
	public int getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String toString() {
		return "Book { "+id+" : "+name+" : "+author+" }";
	}

}
