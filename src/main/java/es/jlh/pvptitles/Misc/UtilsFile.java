/*
 * Copyright (C) 2016 AlternaCraft
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package es.jlh.pvptitles.Misc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class UtilsFile {

    public static boolean exists(String ruta) {
        return new File(ruta).exists();
    }

    public static void writeFile(String ruta, String cont) {
        FileWriter fichero = null;

        try {
            fichero = new FileWriter(ruta);
            fichero.write(cont);
        } catch (Exception e) {
        } finally {
            try {
                // Nuevamente aprovechamos el finally para 
                // asegurarnos que se cierra el fichero.
                if (null != fichero) {
                    fichero.flush();
                    fichero.close();
                }
            } catch (Exception e2) {
            }
        }
    }

    public static String readFile(String ruta) {
        BufferedReader br = null;
        
        try {
            br = new BufferedReader(new FileReader(ruta));

            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }

            return sb.toString();

        } catch (FileNotFoundException ex) {
        } catch (IOException ex) {
        } finally {
            try {                
                br.close();
            } catch (Exception ex) {
            }
        }

        return "";
    }
    
    public static void delete(String ruta) {
        File todelete = new File(ruta);
        if (!todelete.delete()) {
            todelete.deleteOnExit();
        }
    }
}
