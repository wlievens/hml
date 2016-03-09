# Heraldic Markup Language

This project attempts to define an XML schema for describing heraldic *coats of arms*. The first focus is on detailing
and rendering the *escutcheon* (the shield), in later stages this may be expanded to descriptions of the rest of the
heraldic *achievement*.

## Example 1

The following fragment of XML code:

    <?xml version="1.0" encoding="UTF-8"?>
    <coatOfArms xmlns="http://github.com/wlievens/heraldry" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                xsi:schemaLocation="http://github.com/wlievens/heraldry ../src/main/resources/xsd/heraldry.xsd">
        <shape>heater-shield</shape>
        <background>
            <division type="quarterly">
                <part position="1,3">
                    <background>
                        <field tincture="or"/>
                    </background>
                </part>
                <part position="2,4">
                    <background>
                        <field tincture="argent"/>
                    </background>
                </part>
            </division>
        </background>
        <charges>
            <ordinary type="fret" tincture="gules"/>
        </charges>
    </coatOfArms>

Can be turned into the following image:

![Example 1](https://cdn.rawgit.com/wlievens/hml/master/examples/output/example018.svg)

As well as generate the following description in blazon:

*Quarterly or and argent a fret gules*

And yes, I am aware this example shield violates the first rule of tincture...

## Example 2

It is also the intention to implement a wide selection of *variations of the line*, as the following contrived example
already shows:

    <?xml version="1.0" encoding="UTF-8"?>
    <coatOfArms xmlns="http://github.com/wlievens/heraldry" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                xsi:schemaLocation="http://github.com/wlievens/heraldry ../src/main/resources/xsd/heraldry.xsd">
        <shape>heater-shield</shape>
        <background>
            <division type="quarterly">
                <part position="1">
                    <background>
                        <field tincture="argent"/>
                    </background>
                    <charges>
                        <ordinary type="bend-sinister" tincture="azure" line="nebuly"/>
                    </charges>
                </part>
                <part position="2">
                    <background>
                        <field tincture="sable"/>
                    </background>
                    <charges>
                        <ordinary type="bend-sinister" tincture="or" line="invected"/>
                    </charges>
                </part>
                <part position="3">
                    <background>
                        <field tincture="argent"/>
                    </background>
                    <charges>
                        <ordinary type="bend-sinister" tincture="vert" line="wavy"/>
                    </charges>
                </part>
                <part position="4">
                    <background>
                        <field tincture="sable"/>
                    </background>
                    <charges>
                        <ordinary type="bend-sinister" tincture="gules" line="indented"/>
                    </charges>
                </part>
            </division>
        </background>
    </coatOfArms>

This is blazoned as:

*Quarterly first argent a bend sinister nebuly azure, second sable a bend sinister invected or, third argent a bend sinister wavy vert and fourth sable a bend sinister indented gules*

And rendered as:

![Example 2](https://cdn.rawgit.com/wlievens/hml/master/examples/output/example020.svg)
