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
package com.alternacraft.pvptitles.Managers.BoardsCustom;

import com.alternacraft.pvptitles.Files.HologramsFile;
import com.alternacraft.pvptitles.Hook.HolographicHook;
import com.alternacraft.pvptitles.Managers.BoardsAPI.Board;
import com.alternacraft.pvptitles.Managers.BoardsAPI.BoardData;
import com.alternacraft.pvptitles.Managers.BoardsAPI.BoardModel;
import com.alternacraft.pvptitles.Managers.BoardsAPI.ModelController;
import com.alternacraft.pvptitles.Misc.PlayerFame;
import com.alternacraft.pvptitles.Misc.StrUtils;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Location;

public class HologramBoard extends Board {

    private static final double BLOCKSPERCARACTER = 0.153D;
    private static final double BLOCKSPERROW = 0.25D;
    private static final double HEADER = 0.85D;
    private static final double SEPARATOR = 0.3D;

    public static final double DEFAULT_POSITION = 1.5D;

    private final List<Double> xpos = new ArrayList();

    public HologramBoard(BoardData info, BoardModel bm, ModelController mc) {
        super(info, bm, mc);
    }

    // <editor-fold defaultstate="collapsed" desc="LEGACY METHODS">
    @Override
    public boolean isMaterializable(short jugadores) {
        Location l = this.getData().getLocation();

        List<BoardData> holos = HologramsFile.loadHolograms();
        for (BoardData holo : holos) {
            if (l.equals(holo.getLocation())) {
                return false;
            }
        }

        return true;
    }

    @Override
    public void materialize(List<PlayerFame> pf) {

        int maxcol = this.model.getColumnas();

        // Localizacion
        int filas = this.getModel().getFilasSinJugadores(1) + this.getModel().getFilasJugadores(pf.size());

        this.info.setCl(new Location(this.info.getLocation().getWorld(), this.info.getLocation().getX(),
                this.info.getLocation().getY() + (filas * BLOCKSPERROW) + BLOCKSPERROW, this.info.getLocation().getZ()));

        String[][][] resul = (String[][][]) this.modelController.processUnit(pf,
                this.model.getFilasJugadores(pf.size()), 1, this.getModel().isProgresivo());

        Location l = getData().getCustomL();
        Location newL = new Location(l.getWorld(), l.getX(), l.getY(), l.getZ());
        newL.setY(newL.getY() - HEADER);

        int anchomaximo = 0;
        int c = 0;

        List<List<String>> columnacompleta = new ArrayList();
        Double[] blockspercolumn = new Double[maxcol];

        // Convierto la matriz a dos dimensiones
        while (c < maxcol) {
            columnacompleta.add(c, new ArrayList());
            int maxlength = 0;

            for (String[][] fs : resul) {
                for (String[] cs : fs) {
                    if (cs.length <= c) {
                        continue;
                    }

                    String param = cs[c];

                    if (param != null) {
                        if (param.contains("<main>")) {
                            HolographicHook.createHoloBoardHead(l, getModel().getCantidad());
                            param = "";
                        } else {
                            // Longitud para colocar las columnas de la tabla
                            String fix = StrUtils.removeColors(param);
                            if (fix.length() > maxlength) {
                                maxlength = fix.length();
                            }
                        }
                    }

                    columnacompleta.get(c).add(param);
                }
            }

            anchomaximo += maxlength;
            blockspercolumn[c] = maxlength * BLOCKSPERCARACTER;
            c++;
        }

        // Posicion de los hologramas
        if (c == 1) {
            xpos.add(newL.getX());
            HolographicHook.createHoloBoard(columnacompleta.get(0), newL);
        } else {
            // Datos para sacar punto de partida
            double separador = (maxcol - 1) * SEPARATOR;
            double anchobloques = BLOCKSPERCARACTER * anchomaximo + separador;
            double temp = anchobloques / 2;

            newL.setX(newL.getX() - temp);

            // Voy incrementando el tamaño al punto de partida para cada columna
            for (int i = 0; i < columnacompleta.size(); i++) {
                double ant = (i > 0) ? (blockspercolumn[i - 1] / 2) + SEPARATOR : 0;
                newL.setX(newL.getX() + ant + blockspercolumn[i] / 2);
                xpos.add(newL.getX());
                HolographicHook.createHoloBoard(columnacompleta.get(i), newL);
            }
        }
    }

    @Override
    public void dematerialize(short jugadores) {
        Location l = this.getData().getCustomL();
        HolographicHook.deleteHoloBoard(l); // Elimino el header
        for (Double xpo : xpos) {
            // Elimino los hologramas con los datos
            HolographicHook.deleteHoloBoard(new Location(l.getWorld(), xpo, l.getY() - HEADER, l.getZ()));
        }
        xpos.clear();
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="GETTERS">
    @Override
    public BoardData getData() {
        return this.info;
    }

    @Override
    public BoardModel getModel() {
        return this.model;
    }

    @Override
    public ModelController getModelController() {
        return this.modelController;
    }
    // </editor-fold>    
}
