<html>
	<head>
		<link rel="shortcut icon" href="favicon.png">
		<meta charset="utf-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<title>Smart Data Platform - Metadata REST API </title>
		<style>
			body{font-family: arial, sans-serif; letter-spacing: .01em; background-color: #f7f7f7; font-size: 11pt; line-height: 1.5em;margin: 0;}
			header{clear: both; overflow: hidden; margin: 48px  auto 0  auto; width: 80%; }
			header .logo{float: right;}
			header h1{float: left;}
			a{color: #009640;}
			.lang-menu{ position: absolute; top:12px; right: 10%;float: right; font-size: 12px; font-weight: normal; }
			.lang-menu a, .lang-menu strong{margin-left:1em }
			h2{margin-top: 2em; font-size: 32px;}
			.indent{padding-left: 24px;}
			.content{margin: 24px  auto; width: 80%; }
			.row-title td{background-color: #ebebeb; font-weight: bold;}
			.row-title a{padding-left: 2em;}
			.deprecated{color: #bbb; }
			.deprecated pre{border-color: #ccc; background-color: #fafafa;}
			.swagger-deprecated{background: url("./img/swaggerLogoDeprecated.png") center left no-repeat; background-size: 24px 24px; padding-left: 30px; line-height: 24px; display: inline-block; margin-left: 2em;color: #ccc;}

			pre{display: block; padding: 1em; background-color: white; border: solid 1px #009640; }
			table{display: block;  margin-top: 1em;border-collapse: collapse;font-size: 11pt; }
			td, th{ border: solid 1px #ebebeb; padding: .7em;}
			td{background-color: white; }
			th{background-color: #ebebeb; text-align: left; }
			
			.main-menu a{display: inline-block;font-size: 18px;  text-decoration: none; }
			.main-menu a.main-menu-item{ padding: 1em; }
			.main-menu a:hover{ text-decoration: none; background-color: #009640; color: white;}
			.main-menu{border: solid black; border-width: 1px 0; padding: 0;}
			.main-menu a.swagger-menu-item{background: url("./img/swaggerLogoColor.png") 18px center  no-repeat; background-size: 32px 32px; padding: 1em 1em 1em 60px; }
			.main-menu a.swagger-menu-item:hover{background: #009640 url("./img/swaggerLogoWhite.png") 18px center no-repeat;}

			.url-dynamic-parameter-tenant{color:  #60a917; font-weight: bold;}
			.url-dynamic-parameter-smartobject{color:  #0050ef;font-weight: bold;}
			.url-dynamic-parameter-stream{color:  #d80073;font-weight: bold;}
			.url-dynamic-parameter-dataset{color:  #ba00ff;font-weight: bold;}
			.url-dynamic-parameter-package-id{color:  #ff9900;font-weight: bold;}
			ol li {padding: .7em;}
			
			.deprecated .url-dynamic-parameter-tenant,
			.deprecated .url-dynamic-parameter-smartobject,
			.deprecated .url-dynamic-parameter-stream,
			.deprecated .url-dynamic-parameter-dataset,
			.deprecated .url-dynamic-parameter-package-id{color:  #bbb;font-weight: bold;}
			
			footer{background-color: black; color: white; padding: 24px; text-align: center;}
			footer  a{color: white; text-decoration: none; padding: 0 48px;}

		</style>
	</head>
	<body>
		<header>
			<h1>Smartdatanet Metadata API</h1>
			<div class='logo'>
				<img src='https://userportal.smartdatanet.it/userportal/img/familyidentity/logosdp-top-navbar.png' alt='Smartdatanet'/>
			</div>
			<div class='lang-menu'>
				<strong>Italiano</strong>
				<a href='index_en.jsp'>English</a>
			</div>

		</header>
		<div class='content'>
			<p>Esposizione API di ricerca e dettaglio dei Metadati di stream e dataset Contenuti nella Smartdata Platform</p>
			<p class='main-menu'>
				<a href="#search" class='main-menu-item'>Ricerca</a>
				<a href="#detail"span class='main-menu-item'>Dettaglio</a>
				<a href="#auth" class='main-menu-item'>Autenticazione per stream/dataset privati</a>
				<a href="/metadataapi/docs/api/v02/" class='swagger-menu-item'>Swagger Doc</a>
			</p>
			<h2 id="search">Ricerca</h2>
			<div class='indent'>
				<div>
					<strong>URL Base ricerca in formato JSON</strong><pre><code>https://api.smartdatanet.it/metadataapi/api/v02/search?</code></pre>
				</div>
				<div>
					<strong>URL Base ricerca in formato CKAN</strong><pre><code>https://api.smartdatanet.it/metadataapi/api/ckan/2/package_list?</code></pre>
				</div>
				<div>
					<strong>URL Base per generazione di Data Catalog Interoperability Protocol (DCAT-AP-IT) in formato JSON+LD</strong><pre><code>https://api.smartdatanet.it/metadataapi/api/dcat/dataset_list?</code></pre>
				</div>
				<div class='deprecated'>
					<i>V01 (deprecated) URL Base ricerca in formato JSON</i> <a href="/metadataapi/docs/api/v01/" class='swagger-deprecated'>Swagger Doc</a>
					<pre><code>https://api.smartdatanet.it/metadataapi/api/search/full?</code></pre>
				</div>
				<strong>Parametri</strong> 
				<table>
					<thead><tr><th>Key</th><th>Desc</th><th>Valori previsti</th></tr></thead>
					<tbody>
						<tr><td>q</td><td>query di ricerca</td><td>Testo libero</td></tr>
						<tr><td>lang</td><td>Lingua in cui si desidera avere l'ambito del e i tag associati al dataset/stream</td><td>Gestiti italiano (lang=it) e inglese (lang=en)</td></tr>
						<tr class='row-title'><td colspan='3'>Filtri</td></tr>
						<tr><td>tenant</td><td>Codice del tenant proprietario dello stream/dataset</td><td>Se indicato un codice tenant non censito restituisce zero valori </td></tr>
						<tr><td>organization</td><td>Codice dell'organizzazione proprietaria dello stream/dataset</td><td>Se indicato un codice organizzazione non censita restituisce zero valori </td></tr>
						<tr><td>domain</td><td>Codice dell'ambito dello stream/dataset</td><td>Se indicato un codice ambito non censito restituisce zero valori </td></tr>
						<tr><td>opendata</td><td>Propriet&agrave; del dato/stream di essere opendata</td><td>Se indicato opendata=true filtra estraendo i dati opendata, se indicato con valore diverso da true estrae solo i dati NON opendata. Se omesso non filtra sulla propriet&agrave; opendata</td></tr>
						<tr><td>tags</td><td>Elenco dei tag associati al dataset/stream</td><td>Inserire i tagCode separati da virgola</td></tr>
						<tr><td>visibility</td><td>Filtro sul tipo di visibilit&agrave;</td><td>Utilizzare i valori public o private. Non indicare il parametro per averli tutti</td></tr>
						<tr><td>isSearchExact</td><td>Se valorizzato a true effettua la ricerca per parola esatta (invece che lemmattizzata)</td><td>Campo booleano (true/false).  Se omesso vale false.</td></tr>
						<tr><td>includeSandbox</td><td>Indica che &egrave; necessario includere i dati di sandbox (di default non sono presenti)</td><td>Campo booleano (true/false). Se omesso vale false.</td></tr>
						<tr><td>hasStream</td><td>Se a true include solo i dataset/stream che hanno una componente stream (i formati CKAN e DCAT non consentono l'utilizzo di questo parametro) </td><td>Campo booleano (true/false). Non indicare il parametro per averli tutti</td></tr>
						<tr><td>hasDataset</td><td>Se a true include solo i dataset/stream che hanno una componente dataset (i formati CKAN e DCAT non consentono l'utilizzo di questo parametro)</td><td>Campo booleano (true/false). Non indicare il parametro per averli tutti</td></tr>
						<tr><td>externalReference</td><td>Filtra per il campo externalReference (wildcards consentite)</td><td>Campo stringa</td></tr>
						<tr class='row-title'><td colspan='3'>Paginazione</td></tr>
						<tr><td>start</td><td>Paginazione: prima riga da estrarre</td><td>Campo numerico intero (se omesso si parte da zero)</td></tr>
						<tr><td>end</td><td>Paginazione: numero di righe da estrarre</td><td>Campo numerico intero (se omesso vengono estratte 12 righe). Valore massimo: 1000</td></tr>
						<tr class='row-title'><td colspan='3'>Ricerca Geolocalizzata</td></tr>
						<tr><td>minLat</td><td>Minima latitudine del rettangolo contenente gli smartobject</td><td>Campo numerico min=-90.0 max=90.0 </td></tr>
						<tr><td>minLon</td><td>Minima longitudine del rettangolo contenente gli smartobject</td><td>Campo numerico  min=-180.0 max=180.0</td></tr>
						<tr><td>maxLat</td><td>Massima latitudine del rettangolo contenente gli smartobject</td><td>Campo numerico  min=-90.0 max=90.0</td></tr>
						<tr><td>maxLon</td><td>Massima longitudine del rettangolo contenente gli smartobject</td><td>Campo numerico min=-180.0 max=180.0</td></tr>
						<tr class='row-title'><td colspan='3'>Facet <a href=' https://cwiki.apache.org/confluence/display/solr/Faceting' target='_blank'>Documentazione su Apache Solr</a></td></tr>
						<tr><td>facet.field</td><td>Lista di campi separati da virgola su cui calcolare le facet</td><td>Es. tenatCode,domainCode</td></tr>
						<tr><td>facet.prefix</td><td>Limita i termini utilizzati per le facet a quelli che iniziano con il prefisso specificato</td><td>Testo libero</td></tr>
						<tr><td>facet.sort</td><td>Campo secondo cui ordinare il risultato</td><td><code>count</code> valori pi&ugrave; per primi - <code>index</code> in ordine di icdice</tr>
						<tr><td>facet.contains</td><td>Limita i termini utilizzati per le facet a quelli che contengono il testo indicato</td><td>Testo libero</td></tr>
						<tr><td>facet.contains.ignoreCase</td><td>Se utilizzato <code>facet.contains</code> effettua il controllo indipendentemente da maiuscole/minuscole</td><td>Testo libero</td></tr>
						<tr><td>facet.limit</td><td>Numero di elmenti massimi da resituire per ogni facet</td><td>campo numerico</td></tr>
						<tr><td>facet.offset</td><td>Offest da cui iniziare a restituire le facet</td><td>campo numerico</td></tr>
						<tr><td>facet.mincount</td><td>Minimo valore di conteggio che ogni facet deve avere per essere inclusa nella risposta</td><td>campo numerico</td></tr>
						<tr><td>facet.missing</td><td>Vengono conteggiati solo le facet per i campi che non hanno il valore specificato</td><td>Testo libero</td></tr>
					</tbody>
				</table>
			</div>
			<h2 id="detail">Dettaglio</h2>
			<div class='indent'>
				<div>
					<strong>URL Base dettaglio stream in formato JSON</strong><pre><code>https://api.smartdatanet.it/metadataapi/api/v02/detail/<span class='url-dynamic-parameter-tenant'>{{tenantCode}}</span>/<span class='url-dynamic-parameter-smartobject'>{{smartobjectCode}}</span>/<span class='url-dynamic-parameter-stream'>{{streamCode}}</span>?</code></pre>
				dove: 
					<ul>
						<li><span class='url-dynamic-parameter-tenant'>{{tenantCode}}</span> &egrave; il codice dell'organizzazione proprietaria dello stream </li>
						<li><span class='url-dynamic-parameter-smartobject'>{{smartobjectCode}}</span> &egrave; il codice dello Smart Object utilizzato dallo stream</li>
						<li><span class='url-dynamic-parameter-stream'>{{streamCode}}</span> &egrave; il codice dello stream </li>
					</ul>
				</div>
				<div>
				<strong>URL Base dettaglio dataset in formato JSON</strong><pre><code>https://api.smartdatanet.it/metadataapi/api/v02/detail/<span class='url-dynamic-parameter-tenant'>{{tenantCode}}</span>/<span class='url-dynamic-parameter-dataset'>{{datasetCode}}</span></code></pre>
					dove: 
					<ul>
						<li><span class='url-dynamic-parameter-tenant'>{{tenantCode}}</span> &egrave; il codice dell'organizzazione proprietaria dello stream</li>
						<li><span class='url-dynamic-parameter-dataset'>{{datasetCode}}</span> &egrave; il codice del dataset </li>
					</ul>
				</div>
				<div>
					<strong>URL Base dettaglio in formato CKAN</strong><pre><code>https://api.smartdatanet.it/metadataapi/api/ckan/2/package_list/<span class='url-dynamic-parameter-package-id'>{{packageId}}</span></code></pre> 
					dove: 
					<ul>
						<li><span class='url-dynamic-parameter-package-id'>{{packageId}}</span> &egrave; il packageId preso dalla lista restituita dalla ricerca (sempre in formato ckan)</li>
					</ul>
				</div>
				<div  class='deprecated'>
					<i>V01 (deprecated) URL Base dettaglio stream in formato JSON</i> <a href="/metadataapi/docs/api/v01/" class='swagger-deprecated'>Swagger Doc</a>
					<pre><code>https://api.smartdatanet.it/metadataapi/api/detail/<span class='url-dynamic-parameter-tenant'>{{tenantCode}}</span>/<span class='url-dynamic-parameter-smartobject'>{{smartobjectCode}}</span>/<span class='url-dynamic-parameter-stream'>{{streamCode}}</span>?</code></pre>
				</div>
				<div class='deprecated'>
					<i>V01 (deprecated) URL Base dettaglio dataset in formato JSON</i> <a href="/metadataapi/docs/api/v01/" class='swagger-deprecated'>Swagger Doc</a>
					<pre><code>https://api.smartdatanet.it/metadataapi/api/v02/detail/<span class='url-dynamic-parameter-tenant'>{{tenantCode}}</span>/<span class='url-dynamic-parameter-dataset'>{{datasetCode}}</span></code></pre>
				</div>
			
				<strong>Parametri</strong> 
				<table>
					<thead><tr><th>Key</th><th>Desc</th><th>Valori previsti</th></tr></thead>
					<tbody>
						<tr><td>lang</td><td>Lingua in cui si desidera avere l'ambito del e i tag associati al dataset/stream</td><td>Gestiti italiano (lang=it) e inglese (lang=en)</td></tr>
					</tbody>
				</table>
			</div>
			<h2 id="auth">Autenticazione per stream/dataset privati</h2>
			<div class='indent'>
				<div>Per consultare i metadati degli stream/dataset privati &egrave; necessario utilizzare il <strong>token oAuth</strong>
				 durante la chiamata inserendo nell'header HTTP  l'attributo <pre><code>'Authorization'  'Bearer IlMioTokenOauth'</code></pre>
				E' sufficiente utilizzare un qualunque token recuperato da Userportal per poter trovare anche gli stream/dataset privati a cui l'utente associato al token &egrave; abilitato. </div>
				<div>I token sono disponibili nella sezione <cite><strong>Sottoscrizioni</strong></cite> dello <a href='https://userportal.smartdatanet.it/userportal' target='_blank'>Userportal</a> (visibile solo dopo l'autenticazione)</div>
				<div>Ulterioni dettagli sul <a href='http://developer.smartdatanet.it/docs/utilizzo-dello-store-e-dei-token-oauth-2/' target='_blank'>Developer Center</a> nella sezione  
				<strong>Come accedere ai servizi di lettura autenticandosi con Oauth</strong></div>
			</div>
		</div>
		<footer>
			<div>
				<a class="logo-sdp" href="http://www.smartdatanet.it" target='_blank'>Smartdatenet.it</a> | 
				<a class="logo-rp" href="http://www.regione.piemonte.it" target='_blank'>Regione Piemonte</a> | 
				<a class="logo-csi" href="http://www.csipiemonte.it" target='_blank'><span class="hidden">CSI Piemonte</span></a> 
			</div>
		</footer>
	</body>
</html>

