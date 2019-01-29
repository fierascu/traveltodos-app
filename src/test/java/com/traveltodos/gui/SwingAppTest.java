package com.traveltodos.gui;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import com.traveltodos.drools.TravelPlan;
import com.traveltodos.drools.TravelPlanConfiguration;

public class SwingAppTest {
	TravelPlanConfiguration tpc = new TravelPlanConfiguration("/travel-plan-rules.xls");
	

	@Test
	public void testMale() {
		String output = tpc.getWantedValue("Male");
		assertThat(output).isEqualTo("newspaper");
	}
	
	@Test
	public void testFemale() {
		String output = tpc.getWantedValue("Female");
		assertThat(output).isEqualTo("cream");
	}

	@Test
	public void testKids() {
		String output = tpc.getWantedValue("Kids");
		assertThat(output).isEqualTo("medicine");
	}
	
	@Test
	public void testBaby() {
		String output = tpc.getWantedValue("Baby");
		assertThat(output).isEqualTo("diaper");
	}
	
	@Test
	public void testFemaleMale() {
		String output1 = tpc.getWantedValue("Female");
		String output2= tpc.getWantedValue("Male");
		String concat = output1+output2;
		assertThat(concat).isEqualTo("creamnewspaper");
	}
	
	@Test
	public void testFemaleKids() {
		String output1 = tpc.getWantedValue("Female");
		String output2= tpc.getWantedValue("Kids");
		String concat = output1+output2;
		assertThat(concat).isEqualTo("creammedicine");
	}
	
	@Test
	public void testFemaleBaby() {
		String output1 = tpc.getWantedValue("Female");
		String output2= tpc.getWantedValue("Baby");
		String concat = output1+output2;
		assertThat(concat).isEqualTo("creamdiaper");
	}
	
	@Test
	public void testMaleFemale() {
		String output1= tpc.getWantedValue("Male");
		String output2 = tpc.getWantedValue("Female");
		String concat = output1+output2;
		assertThat(concat).isEqualTo("newspapercream");
	}
	
	@Test
	public void testMaleKids() {
		String output1= tpc.getWantedValue("Male");
		String output2 = tpc.getWantedValue("Kids");
		String concat = output1+output2;
		assertThat(concat).isEqualTo("newspapermedicine");
	}
	
	@Test
	public void testMaleBaby() {
		String output1= tpc.getWantedValue("Male");
		String output2 = tpc.getWantedValue("Baby");
		String concat = output1+output2;
		assertThat(concat).isEqualTo("newspaperdiaper");
	}
	
	@Test
	public void testKidsMale() {
		String output1= tpc.getWantedValue("Kids");
		String output2 = tpc.getWantedValue("Male");
		String concat = output1+output2;
		assertThat(concat).isEqualTo("medicinenewspaper");
	}
	
	@Test
	public void testKidsFemale() {
		String output1= tpc.getWantedValue("Kids");
		String output2 = tpc.getWantedValue("Female");
		String concat = output1+output2;
		assertThat(concat).isEqualTo("medicinecream");
	}
	
	@Test
	public void testKidsBaby() {
		String output1= tpc.getWantedValue("Kids");
		String output2 = tpc.getWantedValue("Baby");
		String concat = output1+output2;
		assertThat(concat).isEqualTo("medicinediaper");
	}
	
	@Test
	public void testBabyMale() {
		String output1= tpc.getWantedValue("Baby");
		String output2 = tpc.getWantedValue("Male");
		String concat = output1+output2;
		assertThat(concat).isEqualTo("diapernewspaper");
	}
	
	@Test
	public void testBabyFemale() {
		String output1= tpc.getWantedValue("Baby");
		String output2 = tpc.getWantedValue("Female");
		String concat = output1+output2;
		assertThat(concat).isEqualTo("diapercream");
	}
	
	@Test
	public void testBabyKids() {
		String output1= tpc.getWantedValue("Baby");
		String output2 = tpc.getWantedValue("Kids");
		String concat = output1+output2;
		assertThat(concat).isEqualTo("diapermedicine");
	}
	
	@Test
	public void testMaleFemaleKids() {
		String output1= tpc.getWantedValue("Male");
		String output2 = tpc.getWantedValue("Female");
		String output3 = tpc.getWantedValue("Kids");
		String concat = output1+output2+output3;
		assertThat(concat).isEqualTo("newspapercreammedicine");
	}
	
	@Test
	public void testMaleFemaleBaby() {
		String output1= tpc.getWantedValue("Male");
		String output2 = tpc.getWantedValue("Female");
		String output3 = tpc.getWantedValue("Baby");
		String concat = output1+output2+output3;
		assertThat(concat).isEqualTo("newspapercreamdiaper");
	}
	
	@Test
	public void testMaleKidsBaby() {
		String output1= tpc.getWantedValue("Male");
		String output2 = tpc.getWantedValue("Kids");
		String output3 = tpc.getWantedValue("Baby");
		String concat = output1+output2+output3;
		assertThat(concat).isEqualTo("newspapermedicinediaper");
	}

	
	
}
