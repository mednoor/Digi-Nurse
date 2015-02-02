package com.bt.medication_model;

import java.io.File;
import java.io.Serializable;

import com.bt.utils.Utils;

import android.graphics.Bitmap;

public class Medication implements Serializable {

	private Integer id;
	private String name = "";
	private String picture = "";
	private Bitmap bitmap;
	private Integer deleted = 0;
	// possibility to have : before_food, with_food, after_food,
	// no_food_instructions
	private String food_instruction = "no_food_instructions";
	private String instruction_comment = "";
	private MedicationSchedule medication_schedule;

	public Medication(Integer id, String name, String picture,
			String food_instruction, String instruction_comment) {
		super();
		this.id = id;
		this.name = name;
		this.picture = picture;
		this.food_instruction = food_instruction;
		this.instruction_comment = instruction_comment;
	}

	public Medication() {
		medication_schedule = new MedicationSchedule();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public String getFood_instruction() {
		return food_instruction;
	}

	public void setFood_instruction(String food_instruction) {
		this.food_instruction = food_instruction;
	}

	public String getInstruction_comment() {
		return instruction_comment;
	}

	public void setInstruction_comment(String instruction_comment) {
		this.instruction_comment = instruction_comment;
	}

	public MedicationSchedule getSchedule_medication() {
		return medication_schedule;
	}

	public void setSchedule_medication(MedicationSchedule medication_schedule) {
		this.medication_schedule = medication_schedule;
	}

	public Bitmap getBitmap() {
		return bitmap;
	}

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}

	@Override
	public boolean equals(Object obj) {
		// Vérification de l'égalité des références
		if (obj == this) {
			return true;
		}

		// Vérification du type du paramètre
		if (obj instanceof Medication) {
			// Vérification des valeurs des attributs
			Medication other = (Medication) obj;

			// Pour les attributs de type objets
			// on compare dans un premier temps les références
			if (this.id != other.id) {
				// Si les références ne sont pas identiques
				// on doit en plus utiliser equals()
				if (this.id == null || !this.id.equals(other.id)) {
					return false; // les attributs sont différents
				}
			}
			// Si on arrive ici c'est que tous les attributs sont égaux :
			return true;
		}

		return false;
	}

	public void createBitmap() {
		if (!picture.isEmpty() && bitmap == null) {
			File directory = new File(Utils.PATH_PICTURE_MEDICATION, picture);
			bitmap = Utils.decodeFile(directory.getAbsolutePath());
		}

	}

	public Integer getDeleted() {
		return deleted;
	}

	public void setDeleted(Integer deleted) {
		this.deleted = deleted;
	}

}
