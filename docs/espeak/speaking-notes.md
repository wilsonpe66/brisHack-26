# Creating Voice Effects with ESpeak and Audacity

Generate the source voice clips with ESpeak, then process them in Audacity with the settings shown below.

## Generate the voice clips

```bash
espeak -v en+croak -p 40 -s 160 "More Danger is Coming" -w more-danger-is-coming.wav
espeak -v en+croak -p 40 -s 160 "Taking on Damage Captain" -w taking-on-damage.wav
```

The `en+croak` voice supplies the rough vocal character, `-p 40` lowers the pitch, and `-s 160` sets the speaking rate.

## Apply Audacity effects

Apply the effects in this order.

### Bass and treble

![Audacity bass and treble settings](base-and-treble.png)

### Distortion

![Audacity distortion settings](distortion.png)

### Reverb

![Audacity reverb settings](reverb.png)
