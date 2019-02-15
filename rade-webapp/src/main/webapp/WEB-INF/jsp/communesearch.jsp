<%/*
 *  This file is part of the Rade project (https://github.com/mgimpel/rade).
 *  Copyright (C) 2018 Marc Gimpel
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */%>
<%/* $Id$ */%>
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<jsp:include page="aesn_header.jsp" />
<script>
	$(document).ready(function(){
		loadDepartement();
		$("#formCommune").keydown(function (e) {
			if(e.which == 13) validerForm();
		});
	});

	function loadDepartement() {
		$.getJSON("${pageContext.request.contextPath}/referentiel/commune/json/deptlist",
				{"regionId": $("#codeRegion").val()},
				function(data){
			var current = $("#codeDepartement").val();
			var option = "<option value='-1'>Sélectionner un département...</option>";
			$.each(data, function(key, val) {
				option +=  "<option value='" + key + "'" + (key != current ? ">" : " selected='selected'>") + val + "</option>";
			});
			$("#codeDepartement").html(option);
		});
	}

	function annuler() {
		$("#typeAction").attr("name", "annuler");
		$("#formCommune")[0].submit();
	}

	function validerForm() {
		if(verifierForm()) {
			$("#typeAction").attr("name", "valider");
			$("#formCommune")[0].submit();
		}
	}

	function verifierForm() {
		var nomEnrichi = document.getElementById("nomEnrichi").value;
		var codeRegion = document.getElementById("codeRegion").value;
		var codeDepartement = document.getElementById("codeDepartement").value;
		var circonscription = document.getElementById("codeCirconscription").value;
		var codeInsee = document.getElementById("codeInsee").value;
		if(codeInsee != "" && codeInsee != null) {
			if(!/^[0-9a-zA-Z]{2}[0-9]{3}$/.test(codeInsee)) {
				alert("Le code INSEE doit être composé de cinq chiffres ou de deux lettres et trois chiffres");
				return false;
			}
		}
		var dateEffet = document.getElementById("dateEffet").value;
		if(dateEffet != "" && dateEffet != null){
			if(!/^[0-9]{4}-[0-9]{2}-[0-9]{2}$/.test(dateEffet)) {
				alert("La date n'est pas valide");
				return false;
			}
		}
		if(codeInsee == "" && nomEnrichi == "" && codeRegion == "-1" && codeDepartement == "-1" && circonscription == "-1") {
			alert("Au moins un des champs doit être renseigné");
			return false;
		}
		return true;
	}
</script>
<div class="row justify-content-center">
	<div class="col-12">
		<c:if test="${errorRecherche != null && !errorRecherche.equals('')}">
			<div class="alert alert-danger">${errorRecherche}</div>
		</c:if>
		<div class="card card-aesn">
			<div class="card-body card-body-aesn">
				<form:form id="formCommune" action="${pageContext.request.contextPath}/referentiel/commune/resultats" method="POST" modelAttribute="searchCommune">
					<table class="w-100">
						<tbody style="vertical-align:baseline;">
							<tr>
								<td class="text-right"><form:label path="codeInsee">Code INSEE:</form:label></td>
								<td><form:input class="pt-0 pb-0 pl-1" path="codeInsee"/></td>
							</tr>
							<tr>
								<td class="text-right"><form:label path="nomEnrichi">Nom:</form:label></td>
								<td><form:input class="input-aesn-20 pt-0 pb-0 pl-1" path="nomEnrichi"/></td>
							</tr>
							<tr>
								<td class="text-right"><form:label path="codeRegion">Region:</form:label></td>
								<td>
									<form:select class="input-aesn-20" path="codeRegion" onchange="loadDepartement()">
										<form:option value="-1" label="Sélectionner une région..." />
										<form:options items="${searchCommune.regionsByCodeInsee}" />
									</form:select>
								</td>
							</tr>
							<tr>
								<td class="text-right"><form:label path="codeDepartement">Département:</form:label></td>
								<td>
									<form:select class="input-aesn-20" path="codeDepartement">
										<form:option value="-1" label="Sélectionner un département..." />
										<form:options items="${searchCommune.departementsByCodeInsee}" />
									</form:select>
								</td>
							</tr>
							<tr>
								<td class="text-right"><form:label path="codeCirconscription">Circonscription bassin:</form:label></td>
								<td>
									<form:select class="input-aesn-20" path="codeCirconscription">
										<form:option value="-1" label="Sélectionner une circonscription..." />
										<form:options items="${searchCommune.circonscriptionByCode}" />
									</form:select>
								</td>
							</tr>
							<tr>
								<td class="text-right"><label for="dateEffet">Date d'effet:</label></td>
								<td><form:input type="date" path="dateEffet" /></td>
							</tr>
							<tr>
								<td class="text-right" colspan="2">
									<input type="hidden" name="valider" id="typeAction">
									<input type="button" class="btn btn-sm btn-aesn" value="Annuler" onclick="annuler();">
									<input type="button" class="btn btn-sm btn-aesn" value="Rechercher" onclick="validerForm();">
								</td>
							</tr>
						</tbody>
					</table>
				</form:form>
			</div>
		</div>
	</div>
</div>
<jsp:include page="aesn_footer.jsp" />
