import numpy as np
import pandas as pd

xd = pd.ExcelFile("Book1.xlsx")
df = xd.parse(xd.sheet_names[0])
df = df.replace(np.nan, 'NULL', regex=True)
col_types = df['Type'].values
col_department = df['Department'].values
col_location = df['Location'].values
col_extension = df['Extention'].values
col_other = df['Other extenstions'].values
col_institute = df['Institute'].values
col_details = df['Person associates'].values

for i in range(len(col_types)):
    loc = col_location[i]
    other_string = "new String[]{"
    other = [x.strip() for x in str(col_other[i]).split(",")]
    for o in other:
        if o != 'NULL':
            other_string += "\"" + o + "\","

    if other_string[-1] == ",":
        other_string = other_string[: -1]
    other_string += "}"

    save_format = "{\"%s\", \"%s\", \"%s\", \"%s\", %s, \"%s\",\"%s\"}," % (
        col_types[i], col_department[i].strip(), loc, col_extension[i],
        other_string, col_institute[i], col_details[i])
    print(save_format)
