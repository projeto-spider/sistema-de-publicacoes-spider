package com.spider.auth.models;

import javax.persistence.*;

import com.spider.auth.models.Calendar;

import java.util.Set;

@Entity
@Table(name = "vehicles")
public class Vehicle {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long vehicle_id;

	@Column(name = "type")
	private String type;

	@Column(name = "name")
	private String name;

	@Column(name = "qualis")
	private String qualis;
	
	@Column(name = "line")
	private String line;
        
        @Column(name = "url")
	private String url;
	
	@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE},fetch=FetchType.EAGER)
	@JoinTable(
	  name = "vehicle_calendar", 
	  joinColumns = @JoinColumn(name = "vehicle_id"), 
	  inverseJoinColumns = @JoinColumn(name = "calendar_id"))
	private Set<Calendar> calendar;
//	private Set<Calendar> events = new HashSet<Calendar>();
	
	public Vehicle() {
		super();
	}

	public Vehicle(String type, String name, String qualis, String line, Set<Calendar> calendar, String url) {
		super();
		this.type = type;
		this.name = name;
		this.qualis = qualis;
		this.line = line;
		this.calendar = calendar;
                this.url = url;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getQualis() {
		return qualis;
	}

	public void setQualis(String qualis) {
		this.qualis = qualis;
	}

	public String getLine() {
		return line;
	}

	public void setLine(String line) {
		this.line = line;
	}

	public long getVehicle_id() {
		return vehicle_id;
	}

	public Set<Calendar> getCalendar() {
		return calendar;
	}

	public void setCalendar(Set<Calendar> calendar) {
		this.calendar = calendar;
	}
        
        public String getUrl(){
            return url;
        }
        
        public void setUrl(String url){
             this.url = url;
        }

	@Override
	public String toString() {
		return "Vehicle [id=" + vehicle_id + ", type=" + type + ", name=" + name + ", qualis=" + qualis + ", line =" + line + "]";
	}
}
