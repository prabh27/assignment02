import understand
db = understand.open('/var/app/testdb/test_sonic.db')

for m in db.ents("methods"):
        if m.name.endswith("run"):
                parent = m.ref("DefineIn").ent()
                call = m.refs('Java Callby')
                couple = [item.ent() for item in parent.refs('Couple')]
                if 'Thread' in couple:
                        for item in caller:
                                method = item.ent()
                                filename = item.file()
                                details = (item.line(), item.column())
                                caller = method.ref('DefineIn').ent()
                                relations = caller.refs('Couple')
                                name = [ref.ent().simplename() for ref in relations]
                                print('Method: ', method.longname(), 'In class ', parent.ent(), 'extends from ', filename.longname(), 'in file ', filename, 'at(line, col): ',details
, 'Did you mean to call start() instead?')
                if 'Runnable' in couple:
                        for item in caller:
                                method = item.ent()
                                filename = item.file()
                                details = (item.line(), item.column())
                                caller = method.ref('DefineIn').ent()
                                relations = caller.refs('Couple')
                                name = [ref.ent().simplename() for ref in relations]
                                print('Method: ', method.longname(), 'In class ', parent.ent(), 'extends from ', filename.longname(), 'in file ', filename, 'at(line, col): ',details
, 'Did you mean to call start() instead?')
