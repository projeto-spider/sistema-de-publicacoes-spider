package com.spider.auth.models;

import javax.persistence.*;

@Entity
@Table(name = "publications")
public class Publication {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(name = "qualis")
	private String qualis;
        
        @Column(name = "nome")
	private String nome;
        
        @Column(name = "autor")
        private String autor;
        
        @Column(name = "nomeveiculo")
        private String nomeveiculo;
        
        @Column(name = "year")
        private String year;
        
        @Column(name = "summary")
        private String summary;
        
        @Column(name = "keyword")
        private String keyword;
        
        @Column(name = "researchline")
        private String researchline;
        
        @Column(name = "eventlink")
        private String eventlink;
        
        @Column(name = "type")
        private String type;
        
        public Publication () {
         
        }
        
	//Nome, Login, Senha, Email, Telefone, Aluno
	public Publication(String qualis, String nome, String autor, String nomeveiculo, String year, String summary, String keyword, String researchline, String eventlink, String type) {
		super();
		this.qualis = qualis;
        this.nome = nome;
        this.autor = autor;
        this.nomeveiculo = nomeveiculo;
        this.year = year;
        this.summary = summary;
        this.keyword = keyword;
        this.researchline = researchline;
        this.eventlink = eventlink;
        this.type = type;
        
	}
        
        public long getId() {
		return id;
	}

	public String getQualis () {
		return qualis;
	}

	public void setQualis (String qualis) {
		this.qualis = qualis;
	}
        
	public String getNome () {
		return nome;
	}
        
	public void setNome (String nome) {
		this.nome = nome;
	}
        
	public String getAutor () {
		return autor;
	}
        
	public void setAutor (String autor) {
		this.autor = autor;
	}
	
	public String getNomeveiculo () {
		return nomeveiculo;
	}
        
	public void setNomeveiculo (String nomeveiculo) {
		this.nomeveiculo = nomeveiculo;
	}
        
	public String getYear () {
		return year;
	}
        
        public void setYear (String year) {
                this.year = year;
        }
        
        public String getSummary () {
                return summary;
        }
        
        public void setSummary (String summary) {
                this.summary = summary;
        }
        
        public String getKeyword () {
                return keyword; 
        }
        
        public void setKeyword (String keyword) {
                this.keyword = keyword;
        }
        
        public String getResearchline () {
            return researchline; 
        }
    
        public void setResearchline (String researchline) {
            this.researchline = researchline;
        }
        
        public String getEventlink () {
            return eventlink; 
        }
    
        public void setEventlink (String eventlink) {
            this.eventlink = eventlink;
        }
        
        public String getType () {
            return type; 
        }
    
        public void setType (String type) {
            this.type = type;
        }
    
	@Override
	public String toString() {
		return "Publication [id=" + id + ", qualis=" + qualis + ", nome=" + nome + ", autor=" + autor + ",nomeveiculo=" + nomeveiculo + ", year =" + year + ", summary =" + summary + ", keyword =" + keyword + ", researchline =" + researchline + ", eventlink =" + eventlink + ",type =" + type + "]";
	}
}
