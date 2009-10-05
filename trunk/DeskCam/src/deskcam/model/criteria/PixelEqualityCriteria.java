package deskcam.model.criteria;

import deskcam.model.RGBConstants;

/**
 * Esta interfaz permite implementar distintos criterios para comparar pixeles.
 * Se proveen por default 2 implementaciones:
 * 	- La primera calcula los desvios cuadráticos de cada componente de RGB.
 * 	- La segunda determina si existe algun coeficiente que multiplique a los 3 componentes RGB (cambiando la luminosidad del pixel).
 * 
 * Se pueden agregar nuevos criterios al enum CRITERIAS, siempre y cuando estos tengan un constructor
 * publico que reciba un unico parámetro de tipo int, para ser instanciado mediante reflection.  	 
 * @author Daniel
 */
public interface PixelEqualityCriteria {

	/**
	 * Compara los pixeles px0 y px1, presentados como ints con el formato 0xRRGGBB.
	 * @param px0 
	 * @param px1
	 * @return si los pixeles equivalentes.
	 */
	boolean pxEquals(int px0, int px1);

	public static class RGBMatch implements PixelEqualityCriteria, RGBConstants {
		
		private int tolerance;
		
		public RGBMatch(int tolerance) {
			this.tolerance = tolerance * 50;
		}
		
		@Override
		public boolean pxEquals(int px0, int px1) {
			int dif = ((px0 & RED) - (px1 & RED)) * ((px0 & RED) - (px1 & RED));
			dif += ((px0 & GREEN) - (px1 & GREEN)) * ((px0 & GREEN) - (px1 & GREEN));
			dif += ((px0 & BLUE) - (px1 & BLUE)) * ((px0 & BLUE) - (px1 & BLUE));
			return Math.sqrt(dif) < tolerance;
		}
	};
	
	public static class AdaptableLuminanceCriteria implements PixelEqualityCriteria, RGBConstants {

		private float tolerance;
		
		public AdaptableLuminanceCriteria(int tolerance) {
			this.tolerance = tolerance / 50f;
		}
		
		@Override
		public boolean pxEquals(int px0, int px1) {
			// luminance factors.
			float[] factors = new float[3];
			int[] cs0 = new int[] {px0 & RED, px0 & GREEN, px0 & BLUE};
			int[] cs1 = new int[] {px1 & RED, px1 & GREEN, px1 & BLUE};
			// cs0 always is lighter than cs1.
			if(cs0[0] + cs0[1] + cs0[2] < cs1[0] + cs1[1] + cs1[2]) {
				int[] aux = cs1;
				cs1 = cs0;
				cs0 = aux;
			}
			for (int i = 0; i < 3; i++) {
				factors[i] = (cs0[i] - cs1[i]) / (float)(255 - cs1[i]); 
			}
			return Math.sqrt((factors[0] - factors[1])*(factors[0] - factors[1]) +
					(factors[0] - factors[2])*(factors[0] - factors[2]) +
					(factors[1] - factors[2])*(factors[1] - factors[2])) < tolerance; 
		}
	}
	

	public static enum CRITERIAS {
		ADAPTABLE_LUMINANCE("Adaptable Luminance", AdaptableLuminanceCriteria.class),
		RGB_MATCH("RGB Match", RGBMatch.class);
		private String name;
		private Class<? extends PixelEqualityCriteria> claSs;
		CRITERIAS(String name, Class<? extends PixelEqualityCriteria> claSs) {
			this.name = name;
			this.claSs = claSs;
		}
		@Override
		public String toString() {
			return name;
		}
		public Class<? extends PixelEqualityCriteria> getCriteria() {
			return claSs;
		}
	}
}
